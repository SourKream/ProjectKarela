class CreateVotes < ActiveRecord::Migration
  def change
    create_table :votes do |t|
      t.integer :complaint_id
      t.integer :user_id
      t.integer :vote_type
      t.text :comment

      t.timestamps null: false
    end
  end
end
