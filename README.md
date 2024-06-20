# Intersection over Union (IoU) Sample

![screenshot_1.png](screen/screenshot_1.png)

The Intersection over Union (IoU or Jaccard index) is a metric for measuring the accuracy of an object detection model. Imagine you have two bounding boxes, one predicted bounding box and one ground truth bounding box, and you want some indication of how good the prediction is. That’s where the Intersection over Union comes into place.

Android application, written in Kotlin, built on Clean Architecture principles. Consists of 4 modules:
- App
- Presentation
- Domain
- Data

App module: Hilt modules, unit tests for repository and validator, assemble all module together.

Presentation module: MVVM architecture with view binding, custom GraphView to show chart on the plot with axes. 
There are 2 screens:
- Graph fragment to show Boxes data and Coordinate Plate; Coordinate Plate with Boxes as the 
  only custom view;
- InputData dialog to collect user's coordinate input; with different types of validation: 1st quick immediate validation on presentation layer and 2nd validation on data layer.

Domain module: domain models, repository(ComputationRepository, Validator) interfaces, 2 usecases(GetIntersectionOverUnionUseCase and GetValidationResultUseCase).

Data module: repositories implementation.

Release Notes
v.1.0 - graphic interface, Coordinate plane(x-,y-axis) with Bounding Boxes (GroundBox, Predicted 
and Union) on it, input initial Data through additional dialog screen;

v.2.0 - added DragNDrop functionality for updating initial boxes Data through graphic interface 
(moving boxes along a Coordinate plane)
    
