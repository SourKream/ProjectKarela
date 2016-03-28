json.extract! @complaint, :id, :complaint_type_id, :title, :details, :is_resolved, :group, :admin_users, :action_users, :resolving_users, :created_at, :updated_at
json.upvotes Vote.where(complaint_id: @complaint.id, vote_type: 1).length
json.downvotes Vote.where(complaint_id: @complaint.id, vote_type: -1).length
json.comments @comments
json.user_activity Vote.where(complaint_id: @complaint.id, user_id: current_user.id)