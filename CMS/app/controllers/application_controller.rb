class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :null_session, if: Proc.new { |c| c.request.format.include? 'application/json' }
  include SessionsHelper
  include ComplaintsHelper
  include NotificationsHelper

  #GET /clear
  def clear
  	@complaints = Complaint.all
  	@complaints.each do |complaint|
	  	if is_deletable(complaint.id)
	  		complaint.delete 
	  	end
 	end
  	redirect_to root_path
  end
end
