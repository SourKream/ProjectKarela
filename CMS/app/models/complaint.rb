class Complaint < ActiveRecord::Base
	has_many :votes
	belongs_to :complaint_type
	has_many :notifications
end
