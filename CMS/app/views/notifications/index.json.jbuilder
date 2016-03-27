json.array!(@notification_links) do |notification_link|
  json.extract! notification_link, :id, :notification_id, :is_seen
  json.complaint_id Notification.find(notification_link.notification_id).complaint_id
  json.details Notification.find(notification_link.notification_id).details
  json.url complaint_url(Notification.find(notification_link.notification_id).complaint_id, format: :json)
end
