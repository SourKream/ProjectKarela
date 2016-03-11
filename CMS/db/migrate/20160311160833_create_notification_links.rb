class CreateNotificationLinks < ActiveRecord::Migration
  def change
    create_table :notification_links do |t|
      t.integer :notification_id
      t.integer :user_id
      t.boolean :is_seen

      t.timestamps null: false
    end
  end
end
