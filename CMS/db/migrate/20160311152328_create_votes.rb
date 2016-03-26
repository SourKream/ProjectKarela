class CreateVotes < ActiveRecord::Migration
  def change
    create_table :votes do |t|
      t.belongs_to :complaint, index: true
      t.belongs_to :user, index: true
      t.integer :vote_type
      t.text :comment

      t.timestamps null: false
    end
  end
end
