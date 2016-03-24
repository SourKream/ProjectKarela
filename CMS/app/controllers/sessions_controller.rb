class SessionsController < ApplicationController
  def new
  end
  
  def create
    # authenticate
    user = User.find_by(login_username: params[:session][:login_username])
    # TODO
    if user && user.login_password == params[:session][:login_password]
          # Log the user in and redirect to the user's show page.
          log_in user
          redirect_to user
    else
      #flash[:danger] = 'Invalid email/password combination' # Not quite right! --> return this
      render 'new'
    end
  end
  
  def destroy
    log_out
    redirect_to login_path
  end
end
