json.array!(@users) do |user|
  json.extract! user, :id, :name, :user_type_id, :contact_no, :group, :login_username, :login_password
  json.url user_url(user, format: :json)
end
