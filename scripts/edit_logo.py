#!/usr/bin/env python3
"""Edit logo: remove text, reduce blank space. Keeps only the graphic symbol."""

from PIL import Image
from pathlib import Path

LOGO_PATH = Path(__file__).parent.parent / "frontend" / "src" / "assets" / "logo.png"


def get_content_bbox(img):
    """Find bounding box of non-background pixels (black or transparent)."""
    img_rgba = img.convert("RGBA")
    pixels = img_rgba.load()
    w, h = img_rgba.size
    min_x, min_y = w, h
    max_x, max_y = 0, 0
    for y in range(h):
        for x in range(w):
            r, g, b, a = pixels[x, y]
            # Skip black or nearly transparent
            is_bg = (r < 20 and g < 20 and b < 20) or a < 30
            if not is_bg:
                min_x = min(min_x, x)
                min_y = min(min_y, y)
                max_x = max(max_x, x)
                max_y = max(max_y, y)
    if min_x <= max_x and min_y <= max_y:
        return (min_x, min_y, max_x + 1, max_y + 1)
    return None


def main():
    img = Image.open(LOGO_PATH).convert("RGBA")
    w, h = img.size

    # Find content bounding box to remove outer blank space
    bbox = get_content_bbox(img)
    if bbox:
        img = img.crop(bbox)
        w, h = img.size

    # Crop to keep only the graphic (top ~55%) - remove text at bottom
    crop_height = int(h * 0.55)
    img = img.crop((0, 0, w, crop_height))

    # Make black background transparent
    data = img.getdata()
    new_data = []
    for item in data:
        r, g, b, a = item
        if r < 25 and g < 25 and b < 25:
            new_data.append((0, 0, 0, 0))
        else:
            new_data.append(item)
    img.putdata(new_data)

    # Add minimal padding
    pad = 6
    new_img = Image.new("RGBA", (img.width + pad * 2, img.height + pad * 2), (0, 0, 0, 0))
    new_img.paste(img, (pad, pad))

    # Save
    output_path = LOGO_PATH
    new_img.save(output_path, "PNG")
    print(f"Logo updated: {output_path}")


if __name__ == "__main__":
    main()
