class NotificationLinksController < ApplicationController
  def mark_seen
    if logged_in?
      NotificationLink.find(params[:id].to_i).update(is_seen: true)
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
end