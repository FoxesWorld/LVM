# JavaFX Lottie Animation WebView

A modular and configurable JavaFX component for rendering Lottie animations using a WebView. This project demonstrates best practices in object-oriented design and adheres to SOLID principles, making it flexible, testable, and easy to extend.

## Features

- **Modular Architecture**: Clear separation of concerns across multiple classes (AnimationWebView, HtmlContentBuilder, ResourceLoader, TemplateProcessor).
- **Dynamic HTML Template Processing**: Uses a dedicated `TemplateProcessor` to replace placeholders in HTML templates without hardcoding.
- **Flexible Configuration**: Easily update animation parameters at runtime via `LottieAnimationConfig`.
- **Custom Resource Loading**: Abstracts resource loading using the `ResourceLoader` interface, allowing for custom implementations.
- **Logging & Error Handling**: Integrated logging with SLF4J for better traceability and debugging.


## Requirements

- **Java**: JDK 8 or higher
- **JavaFX**: JavaFX SDK (if using JDK versions that do not bundle JavaFX)
- **SLF4J**: For logging (you may choose your preferred SLF4J binding)

## Installation

Clone the repository and build the project using your preferred build tool.

### Clone the Repository

```bash
git clone [https://github.com/your-username/javafx-lottie-animation-webview.git](https://github.com/FoxesWorld/LVM.git)
cd LVM
