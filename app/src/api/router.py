"""Main API router configuration."""
from fastapi import APIRouter

# Create main router
api_router = APIRouter(prefix="/api", tags=["api"])


@api_router.get("/")
async def api_root():
    """API root endpoint."""
    return {"message": "AI Life Insurance Sales Agent API", "version": "1.0.0"}

