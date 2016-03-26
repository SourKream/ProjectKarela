module NotificationsHelper
  def populate_notifications(params, mode)
    level = ComplaintType.find(params[:complaint][:complaint_type_id])[:level]
    
    # NORMAL notifications
    if mode == "new"
      details = "New complaint posted in group " + @complaint.group.to_s + " by " + current_user.name
    elsif mode == "edit"
      details = "Complaint " + @complaint.title + " edited by " + current_user.name
    end
    
    notif = Notification.create(complaint_id: @complaint.id, details: details)
    res_act = params[:complaint][:resolving_users] + params[:complaint][:action_users]    # resolving + action users
    
    if level==1     
      # personal => send only to resolving and action users  
      receiver_ids = (res_act).uniq # only one per receiver
    elsif level==2  
      # hostel => send to resolving + action users + students of hostel (ASSUMPTION: includes all admin users)
      # using current_user.user_type to avoid variations in spelling of "student" (e.g. "Student")
      receiver_ids = (User.where(group: current_user[:group], user_type: current_user.user_type).pluck(:id) + res_act).uniq
    else            
      # institute => all students + resolving + action users (ASSUMPTION: includes all admin users)
      # using current_user.user_type to avoid variations in spelling of "student" (e.g. "Student")
      receiver_ids = (UserType.find(current_user.user_type).users.pluck(:id) + res_act).uniq
    end
    
    receiver_ids.delete(current_user.id)  # don't notify the user who's posting/editing it duh
    receiver_ids.each do |receiver_id|
      NotificationLink.create(is_seen: false, notification_id: notif.id, user_id: receiver_id)
    end    
    
    # ADMIN notifications
    # send 'you are admin of this' notification to admins on creation
    # UNTESTED
    if mode == "new" and (level==2 or level ==3) and params[:complaint][:admin_users].length>1
      admin_ids = params[:complaint][:admin_users].delete(current_user.id)
      details = "You are admin of " + @complaint.title + " posted by " + current_user.name + " in group " + @complaint.group.to_s
      
      notif = Notification.create(complaint_id: @complaint.id, details: details)
      admin_ids.each do |admin_id|
        NotificationLink.create(is_seen: false, notification_id: notif.id, user_id: admin_id)
      end  
    end      
  end
end
