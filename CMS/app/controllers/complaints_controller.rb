class ComplaintsController < ApplicationController
  before_action :set_complaint, only: [:show, :edit, :update, :destroy]

  # GET /complaints
  # GET /complaints.json
  def index
    # 3 cases : superuser => show all , student => show insti, hostel, self , non student => show only if action/resolving user
    
    if logged_in?
      user_type = UserType.find(current_user.user_type_id).type_name
      
      if user_type.downcase == "superuser"
        @complaints = Complaint.all
      
      elsif user_type.downcase == "student"
        # loading those in which student is admin_user, or same hostel, or institute level
        @complaints = Complaint.where('? = ANY (admin_users) OR "group" = ? OR "group" = ?', *[current_user.id,current_user.group,"institute"])
        
      else
        # non student => show only if resolving or action user
        @complaints = Complaint.where('? = ANY (resolving_users) OR ? = ANY (action_users)', *[current_user.id,current_user.id])
      end
      
    else
      respond_to do |format|
        format.html {redirect_to login_path}
        format.json {render json: []}
      end
    end
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
          populate_new_edit_notifications(params, "new")
          format.html { redirect_to @complaint, notice: 'Complaint was successfully created.' }
          format.json { render :show, status: :created, location: @complaint }
        else
          format.html { render :new }
          format.json { render json: @complaint.errors, status: :unprocessable_entity }
        end
      end
    
    else
      respond_to do |format|
        format.html {redirect_to login_path}
        format.json {render json: {"success" => 0, "user" => user}}
      end
    end
  end

  # PATCH/PUT /complaints/1
  # PATCH/PUT /complaints/1.json
  def update
    respond_to do |format|
      if @complaint.update(complaint_params)
        populate_new_edit_notifications(params, "edit")
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
  
  # GET /complaints/1/mark_resolved
  # GET /complaints/1/mark_resolved.json
  def mark_resolved
    if logged_in? and (Complaint.find(params[:id].to_i).resolving_users.include? current_user.id)
      Complaint.find(params[:id].to_i).update(is_resolved: true)
      populate_resolved_notifications(params[:id].to_i)
      respond_to do |format|
        format.html {redirect_to complaints_path}
        format.json {render json: {"success" => 1, "user" => user}}
      end
    else
      respond_to do |format|
        format.html {redirect_to login_path}
        format.json {render json: {"success" => 0, "user" => user}}
      end
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
      add_current_user_to_admin_users    
      set_complaint_group
      set_action_users
      set_resolving_users  
      
      params[:complaint][:is_resolved] = false
      # snair : added '=> []' in front of those that are arrays, else aren't being permitted          
      params.require(:complaint).permit(:complaint_type_id, :title, :details, :is_resolved, :group, :admin_users => [], :action_users => [], :resolving_users => [])
    end
end
