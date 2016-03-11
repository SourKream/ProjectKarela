require 'test_helper'

class ComplaintsControllerTest < ActionController::TestCase
  setup do
    @complaint = complaints(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:complaints)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create complaint" do
    assert_difference('Complaint.count') do
      post :create, complaint: { action_users: @complaint.action_users, admin_users: @complaint.admin_users, complaint_type_id: @complaint.complaint_type_id, details: @complaint.details, group: @complaint.group, is_resolved: @complaint.is_resolved, resolving_users: @complaint.resolving_users, title: @complaint.title }
    end

    assert_redirected_to complaint_path(assigns(:complaint))
  end

  test "should show complaint" do
    get :show, id: @complaint
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @complaint
    assert_response :success
  end

  test "should update complaint" do
    patch :update, id: @complaint, complaint: { action_users: @complaint.action_users, admin_users: @complaint.admin_users, complaint_type_id: @complaint.complaint_type_id, details: @complaint.details, group: @complaint.group, is_resolved: @complaint.is_resolved, resolving_users: @complaint.resolving_users, title: @complaint.title }
    assert_redirected_to complaint_path(assigns(:complaint))
  end

  test "should destroy complaint" do
    assert_difference('Complaint.count', -1) do
      delete :destroy, id: @complaint
    end

    assert_redirected_to complaints_path
  end
end
