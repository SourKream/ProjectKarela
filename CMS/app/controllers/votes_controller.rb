class VotesController < ApplicationController

	
	def index
		@votes = Vote.all
	end
# debugger

  def new
    @vote = Vote.new
    params[:vote][:complaint_id] = @vote.referer
  end

	def create_vote
		if logged_in?
		@vote = Vote.new(vote_params)
			respond_to do |format|
	        if @vote.save
	          format.html { redirect_to complaint_path, notice: 'Successfully Voted' }
	          format.json { render :show, status: :created, location: @vote }
	        else
	          format.html { render :new }
	          format.json { render json: @vote.errors, status: :unprocessable_entity }
	        end
      end		


    else
      respond_to do |format|
        format.html {redirect_to login_path}
        format.json {render json: {"success" => 0, "user" => user}}
      end
    end
	end

	def create_comment
				
	end

#	def delete_vote
		
#	end

def vote_params
	params[:vote][:user_id] = current_user.id
	params.require(:vote).permit(:vote_type, :comment, :complaint_id, :user_id)
end

end
