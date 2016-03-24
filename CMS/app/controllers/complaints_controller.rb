class ComplaintsController < ApplicationController
  before_action :set_complaint, only: [:show, :edit, :update, :destroy]

  # GET /complaints
  # GET /complaints.json
  def index
    @complaints = Complaint.all
  end

  # GET /complaints/1
  # GET /complaints/1.json
  def show
  end

  # GET /complaints/new
  def new
    @complaint = Complaint.new
  end

  # GET /complaints/1/edit
  def edit
  end

  # POST /complaints
  # POST /complaints.json
  def create
    if logged_in?
      @complaint = Complaint.new(complaint_params)
      
      respond_to do |format|
        if @complaint.save
          format.html { redirect_to @complaint, notice: 'Complaint was successfully created.' }
          format.json { render :show, status: :created, location: @complaint }
        else
          format.html { render :new }
          format.json { render json: @complaint.errors, status: :unprocessable_entity }
        end
      end
    
    else
      redirect_to login_path
      # TODO json message
    end
  end

  # PATCH/PUT /complaints/1
  # PATCH/PUT /complaints/1.json
  def update
    respond_to do |format|
      if @complaint.update(complaint_params)
        format.html { redirect_to @complaint, notice: 'Complaint was successfully updated.' }
        format.json { render :show, status: :ok, location: @complaint }
      else
        format.html { render :edit }
        format.json { render json: @complaint.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /complaints/1
  # DELETE /complaints/1.json
  def destroy
    @complaint.destroy
    respond_to do |format|
      format.html { redirect_to complaints_url, notice: 'Complaint was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_complaint
      @complaint = Complaint.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    # auto-filling missing fields
    def complaint_params  
      
      # setting missing fields
      set_complaint_group
      set_action_users
      #set_resolving_users      
      
      # snair : added '=> []' in front of those which are arrays, else aren't being permitted          
      params.require(:complaint).permit(:complaint_type_id, :title, :details, :is_resolved, :group, :admin_users => [], :action_users => [], :resolving_users => [])
    end
    
    def set_complaint_group   #snair
      # set complaint group (if personal : user_id , else : hostel/insti)
      level = ComplaintType.find(params[:complaint][:complaint_type_id])[:level]
      
      if level == 1             # personal
        params[:complaint][:group] = current_user.id
      elsif level == 2          # hostel
        params[:complaint][:group] = current_user.group         # TODO: to use [:group] (int) or .group (string)
      else
        params[:complaint][:group] = "institute"
      end
    end
    
    def set_action_users    #snair
      # initialize
      # ASSUMPTION: student can not be action user
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
          
          action_users.each do |user|
            # if multiple sweepers etc.
            params[:complaint][:action_users].push(action_user.id)
          end 
        end
      end
    end
end
