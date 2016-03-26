# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)

UserType.create(type_name: 'superuser')
UserType.create(type_name: 'student')
UserType.create(type_name: 'warden')
UserType.create(type_name: 'maintenance secretary')
UserType.create(type_name: 'electrician')
UserType.create(type_name: 'plumber')
UserType.create(type_name: 'sweeper')
UserType.create(type_name: 'ug section')
UserType.create(type_name: 'dean')

User.create(name:'superuser',user_type_id: UserType.find_by(type_name: 'superuser').id,group: 0,login_username: 'su',login_password: 'su')
User.create(name:'surag',user_type_id: UserType.find_by(type_name: 'student').id,group: 1,login_username: 'surag',login_password: 'surag')
User.create(name:'shanti',user_type_id: UserType.find_by(type_name: 'student').id,group: 1,login_username: 'shanti',login_password: 'shanti')
User.create(name:'karan',user_type_id: UserType.find_by(type_name: 'student').id,group: 2,login_username: 'karan',login_password: 'karan')
User.create(name:'kara_boy',user_type_id: UserType.find_by(type_name: 'student').id,group: 2,login_username: 'kara_boy',login_password: 'kara_boy')
User.create(name:'warden_ara',user_type_id: UserType.find_by(type_name: 'warden').id,group: 1,login_username: 'warden_ara',login_password: 'warden_ara')
User.create(name:'maintara',user_type_id: UserType.find_by(type_name: 'maintenance secretary').id,group: 1,login_username: 'maintara',login_password: 'maintara')
User.create(name:'warden_kara',user_type_id: UserType.find_by(type_name: 'warden').id,group: 2,login_username: 'warden_kara',login_password: 'warden_kara')
User.create(name:'maintkara',user_type_id: UserType.find_by(type_name: 'maintenance secretary').id,group: 2,login_username: 'maintkara',login_password: 'maintkara')
User.create(name:'electara',user_type_id: UserType.find_by(type_name: 'electrician').id,group: 1,login_username: 'electara',login_password: 'electara')
User.create(name:'plumbara',user_type_id: UserType.find_by(type_name: 'plumber').id,group: 1,login_username: 'plumbara',login_password: 'plumbara')
User.create(name:'ug boss',user_type_id: UserType.find_by(type_name: 'ug section').id,group: 0,login_username: 'ug',login_password: 'ug')
User.create(name:'dean',user_type_id: UserType.find_by(type_name: 'dean').id,group: 0,login_username: 'dean',login_password: 'dean')

ComplaintType.create(level: 1, type_name: 'tubelight', resolving_user_types: [UserType.find_by(type_name: 'student').id], action_user_types: [UserType.find_by(type_name: 'maintenance secretary').id,UserType.find_by(type_name: 'warden').id,UserType.find_by(type_name: 'electrician').id])
ComplaintType.create(level: 2, type_name: 'toilets', resolving_user_types: [UserType.find_by(type_name: 'warden').id], action_user_types: [UserType.find_by(type_name: 'maintenance secretary').id,UserType.find_by(type_name: 'warden').id,UserType.find_by(type_name: 'plumber').id])
ComplaintType.create(level: 1, type_name: 'fees', resolving_user_types: [UserType.find_by(type_name: 'student').id], action_user_types: [UserType.find_by(type_name: 'ug section').id])
ComplaintType.create(level: 3, type_name: 'LAN ban', resolving_user_types: [UserType.find_by(type_name: 'dean').id], action_user_types: [UserType.find_by(type_name: 'dean').id])