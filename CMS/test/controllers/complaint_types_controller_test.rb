require 'test_helper'

class ComplaintTypesControllerTest < ActionController::TestCase
  setup do
    @complaint_type = complaint_types(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:complaint_types)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create complaint_type" do
    assert_difference('ComplaintType.count') do
      post :create, complaint_type: { action_user_types: @complaint_type.action_user_types, level: @complaint_type.level, resolving_user_types: @complaint_type.resolving_user_types, type_name: @complaint_type.type_name }
    end

    assert_redirected_to complaint_type_path(assigns(:complaint_type))
  end

  test "should show complaint_type" do
    get :show, id: @complaint_type
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @complaint_type
    assert_response :success
  end

  test "should update complaint_type" do
    patch :update, id: @complaint_type, complaint_type: { action_user_types: @complaint_type.action_user_types, level: @complaint_type.level, resolving_user_types: @complaint_type.resolving_user_types, type_name: @complaint_type.type_name }
    assert_redirected_to complaint_type_path(assigns(:complaint_type))
  end

  test "should destroy complaint_type" do
    assert_difference('ComplaintType.count', -1) do
      delete :destroy, id: @complaint_type
    end

    assert_redirected_to complaint_types_path
  end
end
