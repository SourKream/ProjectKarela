class CreateUsers < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :name
      t.integer :user_type_id
      t.string :contact_no
      t.integer :group
      t.string :login_username
      t.string :login_password

      t.timestamps null: false
    end
  end
end
