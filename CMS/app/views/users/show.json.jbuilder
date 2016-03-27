json.extract! @user, :id, :name, :user_type_id, :contact_no, :group, :login_username, :login_password, :created_at, :updated_at
json.user_type UserType.find(@user[:user_type_id]).type_name
