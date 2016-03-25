class SessionsController < ApplicationController
  def new
  end
  
  def create
    # authenticate
    user = User.find_by(login_username: params[:session][:login_username])
    # TODO
    if user && user.login_password == params[:session][:login_password]
      log_in user
      respond_to do |format|
        format.html {redirect_to user}
        format.json {render json: {"success" => 1, "user" => user}}
      end
    else
      respond_to do |format|
        format.html {render 'new'}
        format.json {render json: {"success" => 0, "user" => {}}}
      end
    end    
  end
  
  def destroy
    log_out
    redirect_to login_path
  end
end
