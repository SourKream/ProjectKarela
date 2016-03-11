class ComplaintTypesController < ApplicationController
  before_action :set_complaint_type, only: [:show, :edit, :update, :destroy]

  # GET /complaint_types
  # GET /complaint_types.json
  def index
    @complaint_types = ComplaintType.all
  end

  # GET /complaint_types/1
  # GET /complaint_types/1.json
  def show
  end

  # GET /complaint_types/new
  def new
    @complaint_type = ComplaintType.new
  end

  # GET /complaint_types/1/edit
  def edit
  end

  # POST /complaint_types
  # POST /complaint_types.json
  def create
    @complaint_type = ComplaintType.new(complaint_type_params)

    respond_to do |format|
      if @complaint_type.save
        format.html { redirect_to @complaint_type, notice: 'Complaint type was successfully created.' }
        format.json { render :show, status: :created, location: @complaint_type }
      else
        format.html { render :new }
        format.json { render json: @complaint_type.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /complaint_types/1
  # PATCH/PUT /complaint_types/1.json
  def update
    respond_to do |format|
      if @complaint_type.update(complaint_type_params)
        format.html { redirect_to @complaint_type, notice: 'Complaint type was successfully updated.' }
        format.json { render :show, status: :ok, location: @complaint_type }
      else
        format.html { render :edit }
        format.json { render json: @complaint_type.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /complaint_types/1
  # DELETE /complaint_types/1.json
  def destroy
    @complaint_type.destroy
    respond_to do |format|
      format.html { redirect_to complaint_types_url, notice: 'Complaint type was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_complaint_type
      @complaint_type = ComplaintType.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def complaint_type_params
      params.require(:complaint_type).permit(:level, :type_name, :action_user_types, :resolving_user_types)
    end
end
