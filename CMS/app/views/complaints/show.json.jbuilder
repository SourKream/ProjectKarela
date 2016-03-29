json.extract! @complaint, :id, :complaint_type_id, :title, :details, :is_resolved, :group, :admin_users, :action_users, :resolving_users, :created_at, :updated_at
json.upvotes Vote.where(complaint_id: @complaint.id, vote_type: 1).length
json.downvotes Vote.where(complaint_id: @complaint.id, vote_type: -1).length

json.comments @comments

user_names = []
admin_user_names = []

@comments.each do |comment|
  user_names.push(User.find(comment.user_id).name)
end

@complaint.admin_users.each do |admin_user_id|
  admin_user_names.push(User.find(admin_user_id).name)
end

json.admin_user_names admin_user_names
json.commenter_names user_names
json.user_activity Vote.where(complaint_id: @complaint.id, user_id: current_user.id)

json.is_pokable is_pokable(@complaint[:id])