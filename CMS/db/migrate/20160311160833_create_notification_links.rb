class CreateNotificationLinks < ActiveRecord::Migration
  def change
    create_table :notification_links do |t|
      t.belongs_to :notification, index: true
      t.belongs_to :user, index: true
      t.boolean :is_seen

      t.timestamps null: false
    end
  end
end
