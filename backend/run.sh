#!/bin/bash

# Script to read Gemini API key from application.yaml and set it as environment variable before starting the application

# Get the directory where this script is located (backend directory)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
YAML_FILE="$SCRIPT_DIR/src/main/resources/application.yaml"

if [ ! -f "$YAML_FILE" ]; then
    echo "Error: application.yaml not found at $YAML_FILE"
    exit 1
fi

# Extract API key from YAML file - simple grep and sed approach
# Find the api-key line, extract value, remove comments and quotes
API_KEY=$(grep -E "^[[:space:]]*api-key:[[:space:]]*" "$YAML_FILE" | sed 's/.*api-key:[[:space:]]*\([^#]*\).*/\1/' | sed 's/[[:space:]]*$//' | sed 's/^["'\'']//;s/["'\'']$//' | xargs)

if [ -z "$API_KEY" ] || [ "$API_KEY" = "null" ]; then
    echo "Error: Could not find api-key in $YAML_FILE"
    echo "Please ensure application.yaml contains:"
    echo "  com:"
    echo "    i2gether:"
    echo "      lic:"
    echo "        agent:"
    echo "          api-key: YOUR_API_KEY_HERE"
    exit 1
fi

echo "Setting GEMINI_API_KEY and GOOGLE_API_KEY from application.yaml..."
export GEMINI_API_KEY="$API_KEY"
export GOOGLE_API_KEY="$API_KEY"

# Determine which Maven command to use
if command -v mvn &> /dev/null; then
    MVN_CMD="mvn"
    echo "Using system Maven installation..."
elif [ -f "$SCRIPT_DIR/mvnw" ]; then
    # Check if wrapper is properly configured
    if [ ! -d "$SCRIPT_DIR/.mvn/wrapper" ]; then
        echo "Warning: Maven wrapper found but .mvn/wrapper directory is missing."
        echo "The wrapper may not work properly. Attempting anyway..."
    fi
    MVN_CMD="./mvnw"
    chmod +x "$SCRIPT_DIR/mvnw" 2>/dev/null
    echo "Using Maven wrapper..."
else
    echo "Error: Maven not found and wrapper is not available."
    echo ""
    echo "Please install Maven:"
    echo "  macOS: brew install maven"
    echo "  Or download from: https://maven.apache.org/download.cgi"
    echo ""
    echo "Alternatively, you can set the environment variables manually:"
    echo "  export GEMINI_API_KEY=\"$API_KEY\""
    echo "  export GOOGLE_API_KEY=\"$API_KEY\""
    exit 1
fi

echo "Starting application with API key set..."
cd "$SCRIPT_DIR"
$MVN_CMD spring-boot:run

