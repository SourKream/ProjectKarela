class User < ActiveRecord::Base
	belongs_to :user_type
	has_many :votes
	has_many :notifications, through: :notification_links
end
