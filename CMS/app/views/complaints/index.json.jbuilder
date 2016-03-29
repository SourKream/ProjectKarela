json.array!(@complaints.order('updated_at DESC')) do |complaint|
  json.new_notifs (current_user.notification_links.where(is_seen: false).length > 0)
  json.extract! complaint, :id, :complaint_type_id, :title, :details, :is_resolved, :group, :admin_users, :action_users, :resolving_users
  json.extract! complaint.complaint_type, :level
  json.url complaint_url(complaint, format: :json)
end
