class VotesController < ApplicationController

	
	def index
	   @votes = Vote.all
	end

    def new
	   @vote = Vote.new
	   @@complaint = Complaint.find(params[:complaint])
	end

#POST /complaints/1/vote
#POST /complaints/1/vote.json
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

#POST /complaint/1/comment
#POST /complaint/1/comment.json
	def create_comment
	  if logged_in?
		@vote = Vote.new(comment_params)
		respond_to do |format|
	        if @vote.save
	          format.html { redirect_to complaint_path, notice: 'Successfully Commented' }
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

#	def remove_vote
 #     @vote.destroy
  #    respond_to do |format|
   #   format.html { redirect_to complaints_path, notice: 'Complaint was successfully destroyed.' }
    #  format.json { head :no_content }
   # end
#else
	#@vote.update(vote_type: '0')
#end

#	end

	def vote_params

		#setting missing fields
		params[:vote][:user_id] = current_user.id
		params[:vote][:complaint_id] = @@complaint.id

		#not permitting :comment
		params.require(:vote).permit(:vote_type, :complaint_id, :user_id)
	end


	def comment_params

		#setting missing field
		params[:vote][:user_id] = current_user.id
		params[:vote][:complaint_id] = @@complaint.id

		#not permitting :vote_type
		params.require(:vote).permit(:comment, :complaint_id, :user_id)
	end

end
