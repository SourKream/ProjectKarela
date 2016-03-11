json.array!(@complaint_types) do |complaint_type|
  json.extract! complaint_type, :id, :level, :type_name, :action_user_types, :resolving_user_types
  json.url complaint_type_url(complaint_type, format: :json)
end
