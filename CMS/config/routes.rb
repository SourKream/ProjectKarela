Rails.application.routes.draw do
  #get 'sessions/new' snair: TODO what's this for?

  resources :complaint_types
  resources :user_types
  resources :complaints
  resources :users
  root 'application#home'
  
  get     'notifs'               => 'notifications#index'
  get     'notifs/:id/mark_seen' => 'notification_links#mark_seen'
  get     'notifs/clear_all'     => 'notifications#clear_all',   as: :clear_notifs
  #delete  'notifs/:id'=> 'notification#destroy'
  
  get 'complaints/:id/mark_resolved' => 'complaints#mark_resolved'
  
  get   'login'   => 'sessions#new'
  post  'login'   => 'sessions#create'
  get   'logout'  => 'sessions#destroy'

  get   'view_votes'                  => 'votes#index'
  get   'complaints/:id/vote'         => 'votes#new' , as: :votes
  get   'complaints/:id/comment'      => 'votes#comment' , as: :comments
  post  'complaints/:id/vote'         => 'votes#create_vote'
  post  'complaints/:id/comment'      => 'votes#create_comment'
  get   'complaints/:id/remove_vote'  => 'votes#delete_vote'

  get   'complaints/:id/poke'         => 'notifications#poke'


  
  
  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  # root 'welcome#index'

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'

  # Example of named route that can be invoked with purchase_url(id: product.id)
  #   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase

  # Example resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Example resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Example resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Example resource route with more complex sub-resources:
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', on: :collection
  #     end
  #   end

  # Example resource route with concerns:
  #   concern :toggleable do
  #     post 'toggle'
  #   end
  #   resources :posts, concerns: :toggleable
  #   resources :photos, concerns: :toggleable

  # Example resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end
end
