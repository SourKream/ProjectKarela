class SessionsController < ApplicationController
  def new
  end
  
  def create
    # authenticate
    #user = User.find_by(email: params[:session][:email].downcase)
    # TODO
    if #user && user.password == params[:session][:password]
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
