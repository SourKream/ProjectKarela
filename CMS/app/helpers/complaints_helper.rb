module ComplaintsHelper
  
  def set_complaint_group   #snair
    # set complaint group (if personal : user_id , else : hostel/insti)
    level = ComplaintType.find(params[:complaint][:complaint_type_id])[:level]
    
    if level == 1             # personal
      params[:complaint][:group] = current_user.id
    elsif level == 2          # hostel
      params[:complaint][:group] = current_user.group         # TODO: to use [:group] (int) or .group (string) ?
    else
      params[:complaint][:group] = "institute"
    end
  end
  
  def set_action_users    #snair
    # ASSUMPTION: student can not be action user
    # initialize
    params[:complaint][:action_users] = []
    action_users_type = ComplaintType.find(params[:complaint][:complaint_type_id]).action_user_types
    level = ComplaintType.find(params[:complaint][:complaint_type_id])[:level]
    
    # loop over all action users
    # for some reason, action_users_type will have an extra 'nil' at the end
    action_users_type[0..(action_users_type.length-2)].each do |user_type|
      
      if level == 1 or level == 2
        # personal or hostel complaint => first look for action user of same hostel - if not then look at insti
        # e.g. can include UG sec for personal complaints => will be sent to UG sec (at insti level)
        action_users = User.where(user_type_id: user_type , group: current_user[:group])
        
        if action_users.length == 0
          # ASSUMPTION: no one at hostel level => try insti level (hardcode level for insti = 0) => insti only if NOT hostel
          action_users = User.where(user_type_id: user_type , group: 0)
        end

        action_users.each do |action_user|
          # loop for multiple sweepers etc., if in case
          params[:complaint][:action_users].push(action_user.id)
        end          
      
      else
        # insti level complaint
        # ASSUMPTION: insti action users are at insti level only
        action_users = User.where(user_type_id: user_type , group: 0)
        
        action_users.each do |action_user|
          # if multiple sweepers etc.
          params[:complaint][:action_users].push(action_user.id)
        end 
      end
    end
  end
  
  def set_resolving_users    #snair
    # initialize
    # mostly similar to set_action_users, but students can be resolving users => concatenate admin users
    params[:complaint][:resolving_users] = []
    resolving_users_type = ComplaintType.find(params[:complaint][:complaint_type_id]).resolving_user_types
    level = ComplaintType.find(params[:complaint][:complaint_type_id])[:level]

    # loop over all resolving user types
    # for some reason, resolving_users_type will have an extra 'nil' at the end
    resolving_users_type[0..(resolving_users_type.length-2)].each do |user_type|
      
      if UserType.find(user_type).type_name.downcase == "student"
        # concatenate list of admin users
        params[:complaint][:resolving_users].concat(params[:complaint][:admin_users])
        
      elsif level == 1 or level == 2
        # personal or hostel complaint => first look for resolving user of same hostel - if not then look at insti
        # e.g. can include UG sec for personal complaints => will be sent to UG sec (at insti level)
        resolving_users = User.where(user_type_id: user_type , group: current_user[:group])
        
        if resolving_users.length == 0
          # ASSUMPTION: no one at hostel level => try insti level (hardcode level for insti = 0) => insti only if NOT hostel
          resolving_users = User.where(user_type_id: user_type , group: 0)
        end

        resolving_users.each do |resolving_user|
          # loop for multiple sweepers etc., if in case
          params[:complaint][:resolving_users].push(resolving_user.id)
        end          
      
      else
        # insti level complaint
        # ASSUMPTION: insti resolving users are at insti level only
        resolving_users = User.where(user_type_id: user_type , group: 0)
        
        resolving_users.each do |resolving_user|
          # if multiple sweepers etc.
          params[:complaint][:resolving_users].push(resolving_user.id)
        end 
      end
    end
  end
  
  def add_current_user_to_admin_users   #snair
    # ASSUMPTION: id of current user is not in admin_users
    
    if params[:complaint][:admin_users].class != Array
      # current user is the only admin_user
      # ASSUMPTION: else must be array
      params[:complaint][:admin_users] = []
    end
    
    params[:complaint][:admin_users].push(current_user.id)
  end  
  
  def is_pokable(id)    #snair
    (Time.now - Complaint.find(id).updated_at.to_time) > 60
  end
  
end
