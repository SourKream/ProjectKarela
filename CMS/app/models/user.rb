class User < ActiveRecord::Base
	belongs_to :user_type
	has_many :votes
	has_many :notification_links
	has_many :notifications, through: :notification_links
	enum group: {institute: 0, aravali: 1, karakoram: 2, nilgiri: 3}
end
