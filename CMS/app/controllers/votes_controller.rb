class VotesController < ApplicationController

#GET /view_votes
	def index
	   @votes = Vote.all
	end

#GET /complaints/1/votes
	def complaint_index
	   @votes = Vote.where(complaint_id: params[:id])
	end

#GET /complaints/1/vote
  def new
	   @vote = Vote.new
	end

#GET /complaints/1/comment
	def comment
	   @vote = Vote.new
	end

#POST /complaints/1/vote
#POST /complaints/1/vote.json
	def create_vote
		if logged_in?
			@vote = Vote.find_by(user_id: current_user.id, complaint_id: params[:id])

			#check if vote already exists or not

			if @vote == nil
				@vote = Vote.new(vote_params)
				respond_to do |format|
				    if @vote.save
				      format.html { redirect_to complaint_path, notice: 'Successfully Voted' }
				      format.json { render json: {"success" => 1}}
				    else
				      format.html { render :new }
				      format.json { render json: {"success" => 0}}
				    end
			    end	
			else
		    respond_to do |format|
		      if @vote.update_attributes(vote_params)
		        format.html { redirect_to complaint_path, notice: 'Vote was successfully updated.' }
		        format.json {render json: {"success" => 1}}
		      else
		        format.html { render :edit }
		        format.json { render json: @vote.errors, status: :unprocessable_entity }
		      end
			  end
	    end
      
		else
	    respond_to do |format|
		    format.html {redirect_to login_path}
		    format.json {render json: {"success" => 0}}
	    end
	  end
  end

#POST /complaint/1/comment
#POST /complaint/1/comment.json
	def create_comment
		if logged_in?
			@vote = Vote.find_by(user_id: current_user.id, complaint_id: params[:id])
			if @vote == nil
				@vote = Vote.new(comment_params)
				respond_to do |format|
				    if @vote.save
				       format.html { redirect_to complaint_path, notice: 'Successfully Voted' }
				       format.json {render json: {"success" => 1}}
				    else
				       format.html { render :new }
				       format.json {render json: {"success" => 0}}
				    end
			    end	
			else
		        respond_to do |format|
		      	    if @vote.update_attributes(comment_params)
				       format.html { redirect_to complaint_path, notice: 'Vote was successfully updated.' }
				       format.json {render json: {"success" => 1}}
				    else
				       format.html { render :edit }
				       format.json {render json: {"success" => 0}}
				    end
			    end
	     	end
		else
		    respond_to do |format|
		       format.html {redirect_to login_path}
		       format.json {render json: {"success" => 0, "user" => user}}
	        end
	    end
  	end

#GET /complaint/1/remove_vote
#GET /complaint/1/remove_vote.json
	def delete_vote
		if logged_in?
			@vote = Vote.find_by(user_id: current_user.id, complaint_id: params[:id])

			#if vote doesn't exist redirect to the complaint, else set the vote_type to 0

			if @vote == nil
				redirect_to complaint_path
			else
		        respond_to do |format|
		      	    if @vote.update_attributes(vote_type: 0)
				       format.html { redirect_to complaint_path, notice: 'Vote was successfully updated.' }
				       format.json {render json: {"success" => 1}}
				    else
				       format.html { render :edit }
				       format.json { render json: @vote.errors, status: :unprocessable_entity }
				    end
			    end
	     	end
		else
		    respond_to do |format|
		       format.html {redirect_to login_path}
		       format.json {render json: {"success" => 0}}
	        end
	    end
  	end


	def vote_params
		#setting missing fields
		params[:vote][:user_id] = current_user.id
		params[:vote][:complaint_id] = params[:id]

		#not permitting :comment
		params.require(:vote).permit(:vote_type, :complaint_id, :user_id)
	end


	def comment_params
		#setting missing field
		params[:vote][:user_id]       = current_user.id
    	params[:vote][:vote_type]     = 0
		params[:vote][:complaint_id]  = params[:id]

		#not permitting :vote_type
		params.require(:vote).permit(:comment, :complaint_id, :user_id)
	end

end
