class CreateUsers < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :name
      t.belongs_to :user_type, index: true
      t.string :contact_no
      t.integer :group
      t.string :login_username
      t.string :login_password

      t.timestamps null: false
    end
  end
end
