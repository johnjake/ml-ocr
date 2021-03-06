# Reader Calculator   ![alt text](https://github.com/johnjake/ml-ocr/blob/master/calc.png)

ReaderCalculator - allow the user to capture arithmetic expressions (i.e. 1+2) either directly from the built-in camera or from an image file picked by the user from the filesystem. Once the input is provided the app detect the expression in the picture and compute its result.

Used in Development: 

    IDE: Android Studio Chipmunk | 2021.2.1 Patch 1
    VM: OpenJDK 64-Bit

    Gradle Version: 7.1.3
    Kotlin Version: 1.6.10
    Gradle Type: DSL

    Language: Kotlin
    Architechture Pattern: MVVM
    Type: Modular
    
    Unit Test: 
      ● ViewModelTest, 
      ● RepositoryTest, 
      ● ExtensionTest
    
    Code Formatting: 
      ● Spotless 
      ● Ktlint
    
    Note: 
    ktlint pre-commit script will only work and copied to <directory>\ml-ocr\.git\hooks
    upon build/run app or using ./gradlew build.
    
    Features Covered:
    ● Support of multiple app variants.
    ● Controlling the behavior of the app at compile time
    ● Handling different themes.
    ● Integration with a 3rd party library
    ● Integration with the system (file picker, camera)
    ● Permission handling (file picker, camera)

   
   User Interface:
   
      Settings
   
  ![alt text](https://github.com/johnjake/ml-ocr/blob/master/settings.png)
   
      File System
   
  ![alt text](https://github.com/johnjake/ml-ocr/blob/master/file_system.png)
  
      Camera System
  
  ![alt text](https://github.com/johnjake/ml-ocr/blob/master/camera_system.png)
  



