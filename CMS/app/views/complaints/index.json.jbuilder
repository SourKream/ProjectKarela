json.array!(@complaints) do |complaint|
  json.extract! complaint, :id, :complaint_type_id, :title, :details, :is_resolved, :group, :admin_users, :action_users, :resolving_users
  json.url complaint_url(complaint, format: :json)
end
