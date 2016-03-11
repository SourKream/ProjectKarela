class CreateComplaintTypes < ActiveRecord::Migration
  def change
    create_table :complaint_types do |t|
      t.integer :level
      t.string :type_name
      t.integer :action_user_types, :array => true
      t.integer :resolving_user_types, :array => true

      t.timestamps null: false
    end
  end
end
