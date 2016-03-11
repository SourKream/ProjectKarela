class Notification < ActiveRecord::Base
	belongs_to :complaint
	has_many :notification_links
	has_many :users, through: :notification_links
end