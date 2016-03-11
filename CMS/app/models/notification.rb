class Notification < ActiveRecord::Base
	belongs_to :complaint
	has_many :users, through: :notification_links
end