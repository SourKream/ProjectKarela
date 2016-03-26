# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20160311160833) do

  # These are extensions that must be enabled in order to support this database
  enable_extension "plpgsql"

  create_table "complaint_types", force: :cascade do |t|
    t.integer  "level"
    t.string   "type_name"
    t.integer  "action_user_types",                 array: true
    t.integer  "resolving_user_types",              array: true
    t.datetime "created_at",           null: false
    t.datetime "updated_at",           null: false
  end

  create_table "complaints", force: :cascade do |t|
    t.integer  "complaint_type_id"
    t.string   "title"
    t.text     "details"
    t.boolean  "is_resolved"
    t.string   "group"
    t.integer  "admin_users",                    array: true
    t.integer  "action_users",                   array: true
    t.integer  "resolving_users",                array: true
    t.datetime "created_at",        null: false
    t.datetime "updated_at",        null: false
  end

  create_table "notification_links", force: :cascade do |t|
    t.integer  "notification_id"
    t.integer  "user_id"
    t.boolean  "is_seen"
    t.datetime "created_at",      null: false
    t.datetime "updated_at",      null: false
  end

  add_index "notification_links", ["notification_id"], name: "index_notification_links_on_notification_id", using: :btree
  add_index "notification_links", ["user_id"], name: "index_notification_links_on_user_id", using: :btree

  create_table "notifications", force: :cascade do |t|
    t.integer  "complaint_id"
    t.text     "details"
    t.datetime "created_at",   null: false
    t.datetime "updated_at",   null: false
  end

  create_table "user_types", force: :cascade do |t|
    t.string   "type_name"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  create_table "users", force: :cascade do |t|
    t.string   "name"
    t.integer  "user_type_id"
    t.string   "contact_no"
    t.integer  "group"
    t.string   "login_username"
    t.string   "login_password"
    t.datetime "created_at",     null: false
    t.datetime "updated_at",     null: false
  end

  create_table "votes", force: :cascade do |t|
    t.integer  "complaint_id"
    t.integer  "user_id"
    t.integer  "vote_type"
    t.text     "comment"
    t.datetime "created_at",   null: false
    t.datetime "updated_at",   null: false
  end

end
