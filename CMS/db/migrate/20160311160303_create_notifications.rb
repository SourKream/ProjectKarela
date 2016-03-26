class CreateNotifications < ActiveRecord::Migration
  def change
    create_table :notifications do |t|
      t.belongs_to :complaint, index: true
      t.text :details

      t.timestamps null: false
    end
  end
end
