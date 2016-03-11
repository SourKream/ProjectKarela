class ComplaintType < ActiveRecord::Base
	has_many :complaints
	enum level: {personal: 1, hostel: 2, institute: 3}
end
