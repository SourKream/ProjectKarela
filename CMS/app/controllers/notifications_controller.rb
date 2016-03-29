class NotificationsController < ApplicationController
  def index
    if logged_in?
      user_type = UserType.find(current_user.user_type_id).type_name
      
      if user_type.downcase == "superuser"
        @notification_links = NotificationLink.all
      
      else
        # student/non student
        @notification_links = current_user.notification_links
      end
      
    else
      respond_to do |format|
        format.html {redirect_to login_path}
        format.json {render json: {"success" => 0}}
      end
    end
  end
  
  def clear_all
    if logged_in?
      current_user.notification_links.each do |nl|
        nl.destroy
      end
      
      respond_to do |format|
        format.html {redirect_to notifs_path}
        format.json {render json: {"success" => 1}}
      end
      
    else
      respond_to do |format|
        format.html {redirect_to login_path}
        format.json {render json: {"success" => 0}}
      end
    end
  end

  def poke
    if logged_in? and is_pokable(params[:id])
    populate_poke_notifications(params[:id])
    Complaint.find(params[:id]).update(updated_at: Time.now)
     respond_to do |format|
        format.html {redirect_to complaints_path}
        format.json {render json: {"success" => 1}}
      end
    else
      respond_to do |format|
        format.html {redirect_to login_path}
        format.json {render json: {"success" => 0}}
      end
    end
  end
  
end
