# Simple Notes
Notes keeping Android application, written in Kotlin

## Libraries and Services Used
* **Room** - local database functions
* **Jetpack Navigation Components** - navigation functionality between elements of the application
* **Material Design Components** - standardised UI design

## Architectural Approach
* Model-View-ViewModel structure
* Data-Domain-Presentation package breakdown

## UI Elements
* Navigation components graph to support navigation between fragments and activities (including with arguments)
* Shared view models to support passing back of data/notifications to previous destinations
* RecyclerView using LinearLayoutManager and StaggeredGridLayoutManager
* ItemTouchHelper for swipe to delete callback for RecyclerView
* TextFilter for searching results within RecyclerView
* Action bar with menu
* Custom dialog layout
* Dark mode

## Other Application Elements
* Preferences dialog for storing/retrieving user settings
