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
      redirect_to login_path
      # TODO json message
    end
  end
end
