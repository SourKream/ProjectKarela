class CreateComplaints < ActiveRecord::Migration
  def change
    create_table :complaints do |t|
      t.integer :complaint_type_id
      t.string :title
      t.text :details
      t.boolean :is_resolved
      t.string :group
      t.integer :admin_users, :array => true 
      t.integer :action_users, :array => true
      t.integer :resolving_users, :array => true

      t.timestamps null: false
    end
  end
end
