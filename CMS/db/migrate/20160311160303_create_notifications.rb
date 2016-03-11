class CreateNotifications < ActiveRecord::Migration
  def change
    create_table :notifications do |t|
      t.integer :complaint_id
      t.text :details

      t.timestamps null: false
    end
  end
end
