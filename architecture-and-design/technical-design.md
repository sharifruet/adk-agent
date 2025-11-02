# Technical Design Document (TDD)
## AI Life Insurance Sales Agent Application

**Version**: 1.0  
**Last Updated**: [Date]  
**Authors**: Development Team  
**Status**: Draft

---

## Table of Contents

1. [Document Purpose](#document-purpose)
2. [Component Detailed Designs](#component-detailed-designs)
3. [Data Structures and Models](#data-structures-and-models)
4. [API Specifications](#api-specifications)
5. [Database Schema Design](#database-schema-design)
6. [Algorithms and Logic](#algorithms-and-logic)
7. [State Management](#state-management)
8. [Error Handling](#error-handling)
9. [Security Implementation](#security-implementation)
10. [LLM Integration Details](#llm-integration-details)
11. [Validation Logic](#validation-logic)
12. [Performance Optimizations](#performance-optimizations)

---

## Document Purpose

This document provides detailed technical specifications for implementing the AI Life Insurance Sales Agent Application. It includes component designs, algorithms, data structures, API specifications, and implementation guidelines.

**Intended Audience**:
- Backend developers
- Frontend developers
- Database developers
- DevOps engineers
- Code reviewers

**Related Documents**:
- System Architecture Document: `/architecture-and-design/system-architecture.md`
- Requirements Document: `/requirements/requirements.md`

---

## Component Detailed Designs

### 2.1 Conversation Service

#### 2.1.1 Class Structure

```python
class ConversationService:
    def __init__(
        self,
        llm_provider: LLMProvider,
        session_manager: SessionManager,
        policy_service: PolicyService,
        validation_service: ValidationService,
        lead_service: LeadService
    ):
        self.llm_provider = llm_provider
        self.session_manager = session_manager
        self.policy_service = policy_service
        self.validation_service = validation_service
        self.lead_service = lead_service
    
    async def start_conversation(self) -> ConversationResponse:
        """Initialize new conversation session"""
        
    async def process_message(
        self, 
        session_id: str, 
        user_message: str
    ) -> ConversationResponse:
        """Process user message and generate response"""
        
    async def end_conversation(
        self, 
        session_id: str,
        reason: Optional[str] = None
    ) -> None:
        """Gracefully end conversation"""
        
    def detect_intent(self, message: str, context: Dict) -> Intent:
        """Analyze customer intent from message"""
        
    def detect_interest(self, conversation: Conversation) -> InterestLevel:
        """Detect buying interest signals"""
```

#### 2.1.2 Process Message Algorithm

```python
async def process_message(session_id: str, user_message: str):
    # 1. Load session and context
    session = await session_manager.get_session(session_id)
    if not session:
        raise SessionNotFoundError(session_id)
    
    # 2. Validate and sanitize input
    sanitized_message = sanitize_input(user_message)
    
    # 3. Save user message to conversation log
    await conversation_repository.add_message(
        conversation_id=session.conversation_id,
        role="user",
        content=sanitized_message
    )
    
    # 4. Detect intent
    intent = await detect_intent(sanitized_message, session.context)
    
    # 5. Check for exit signals
    if is_exit_signal(sanitized_message, intent):
        return await handle_exit(session_id)
    
    # 6. Update customer profile if new information
    extracted_data = extract_information(sanitized_message, session.context)
    if extracted_data:
        session.customer_profile.update(extracted_data)
        await session_manager.update_session(session_id, session)
    
    # 7. Check conversation stage and handle accordingly
    stage = determine_stage(session, intent)
    
    if stage == ConversationStage.INFORMATION_COLLECTION:
        return await handle_information_collection(session, sanitized_message)
    elif stage == ConversationStage.POLICY_PRESENTATION:
        return await handle_policy_presentation(session, sanitized_message)
    elif stage == ConversationStage.OBJECTION_HANDLING:
        return await handle_objection(session, sanitized_message)
    
    # 8. Build context for LLM
    context = build_llm_context(session, extracted_data)
    
    # 9. Generate response using LLM
    llm_response = await llm_provider.generate_response(
        messages=context.messages,
        customer_profile=session.customer_profile,
        available_policies=await get_relevant_policies(session.customer_profile)
    )
    
    # 10. Filter and process response
    filtered_response = filter_response(llm_response)
    
    # 11. Detect interest in response
    interest_level = detect_interest_from_response(filtered_response, session)
    
    # 12. Save assistant message
    await conversation_repository.add_message(
        conversation_id=session.conversation_id,
        role="assistant",
        content=filtered_response
    )
    
    # 13. Update session state
    session.last_activity = datetime.utcnow()
    session.message_count += 1
    await session_manager.update_session(session_id, session)
    
    # 14. Return response
    return ConversationResponse(
        message=filtered_response,
        session_id=session_id,
        interest_detected=interest_level,
        next_stage=stage
    )
```

#### 2.1.3 Conversation Stage Management

```python
class ConversationStage(Enum):
    INTRODUCTION = "introduction"
    QUALIFICATION = "qualification"
    INFORMATION = "information"
    PERSUASION = "persuasion"
    OBJECTION_HANDLING = "objection_handling"
    INFORMATION_COLLECTION = "information_collection"
    CLOSING = "closing"
    ENDED = "ended"

def determine_stage(session: Session, intent: Intent) -> ConversationStage:
    """Determine current conversation stage based on session state and intent"""
    
    if session.conversation_stage == ConversationStage.ENDED:
        return ConversationStage.ENDED
    
    # Check if information collection is ready
    if session.interest_detected == InterestLevel.HIGH:
        if not session.collected_data_complete:
            return ConversationStage.INFORMATION_COLLECTION
    
    # Check for objections
    if intent == Intent.OBJECTION:
        return ConversationStage.OBJECTION_HANDLING
    
    # Check profile completeness for qualification
    if session.conversation_stage == ConversationStage.INTRODUCTION:
        if is_profile_complete(session.customer_profile):
            return ConversationStage.INFORMATION
        return ConversationStage.QUALIFICATION
    
    # Default progression
    return session.conversation_stage
```

#### 2.1.4 Interest Detection Algorithm

```python
class InterestLevel(Enum):
    NONE = "none"
    LOW = "low"
    MEDIUM = "medium"
    HIGH = "high"

def detect_interest(conversation: Conversation) -> InterestLevel:
    """Detect customer interest level from conversation"""
    
    score = 0
    
    # Analyze recent messages for buying signals
    recent_messages = conversation.get_recent_messages(count=10)
    
    # Positive signals
    positive_keywords = [
        "interested", "I want", "I'll take", "sign up", 
        "register", "apply", "how do I", "what's next",
        "that sounds good", "let's do it"
    ]
    
    for message in recent_messages:
        if message.role == "user":
            content_lower = message.content.lower()
            for keyword in positive_keywords:
                if keyword in content_lower:
                    score += 2
    
    # Question patterns indicating interest
    if contains_next_steps_question(recent_messages):
        score += 3
    
    # Policy selection
    if contains_policy_selection(recent_messages):
        score += 5
    
    # Negative signals (reduce score)
    negative_keywords = ["not interested", "no thanks", "maybe later"]
    for message in recent_messages:
        if message.role == "user":
            content_lower = message.content.lower()
            for keyword in negative_keywords:
                if keyword in content_lower:
                    score -= 3
    
    # Determine level
    if score >= 8:
        return InterestLevel.HIGH
    elif score >= 5:
        return InterestLevel.MEDIUM
    elif score >= 2:
        return InterestLevel.LOW
    else:
        return InterestLevel.NONE
```

---

### 2.2 LLM Integration Component

#### 2.2.1 LLM Provider Abstraction

```python
from abc import ABC, abstractmethod
from typing import List, Dict, Any, Optional

class LLMProvider(ABC):
    """Abstract base class for LLM providers"""
    
    @abstractmethod
    async def generate_response(
        self,
        messages: List[Message],
        config: LLMConfig
    ) -> LLMResponse:
        """Generate response from LLM"""
        pass
    
    @abstractmethod
    async def classify_intent(self, message: str) -> Intent:
        """Classify user intent"""
        pass
    
    @abstractmethod
    async def extract_entities(
        self, 
        message: str,
        entity_types: List[str]
    ) -> Dict[str, Any]:
        """Extract structured entities from message"""
        pass

class LLMConfig:
    temperature: float = 0.7
    max_tokens: int = 500
    top_p: float = 1.0
    frequency_penalty: float = 0.0
    presence_penalty: float = 0.0
```

#### 2.2.2 OpenAI Provider Implementation

```python
import openai
from typing import List

class OpenAIProvider(LLMProvider):
    def __init__(self, api_key: str, model: str = "gpt-4"):
        self.client = openai.AsyncOpenAI(api_key=api_key)
        self.model = model
    
    async def generate_response(
        self,
        messages: List[Message],
        config: LLMConfig
    ) -> LLMResponse:
        """Generate response using OpenAI API"""
        
        try:
            # Build OpenAI message format
            openai_messages = self._convert_messages(messages)
            
            # Add system prompt
            system_prompt = self._build_system_prompt()
            openai_messages.insert(0, {
                "role": "system",
                "content": system_prompt
            })
            
            # Call API with retry logic
            response = await self._call_with_retry(
                self.client.chat.completions.create,
                model=self.model,
                messages=openai_messages,
                temperature=config.temperature,
                max_tokens=config.max_tokens,
                top_p=config.top_p,
                frequency_penalty=config.frequency_penalty,
                presence_penalty=config.presence_penalty
            )
            
            return LLMResponse(
                content=response.choices[0].message.content,
                model=self.model,
                tokens_used=response.usage.total_tokens,
                finish_reason=response.choices[0].finish_reason
            )
            
        except openai.RateLimitError:
            raise LLMRateLimitError("OpenAI rate limit exceeded")
        except openai.APIError as e:
            raise LLMAPIError(f"OpenAI API error: {str(e)}")
    
    async def _call_with_retry(self, func, max_retries=3, **kwargs):
        """Retry API call with exponential backoff"""
        for attempt in range(max_retries):
            try:
                return await func(**kwargs)
            except Exception as e:
                if attempt == max_retries - 1:
                    raise
                wait_time = 2 ** attempt
                await asyncio.sleep(wait_time)
    
    def _build_system_prompt(self) -> str:
        """Build system prompt with agent persona and guidelines"""
        return f"""
You are an AI life insurance sales agent for [Company Name].
Your role is to help customers understand life insurance options and find suitable coverage.

Guidelines:
- Be professional, friendly, and empathetic
- Use persuasive techniques naturally (not aggressive)
- Be transparent that you're an AI assistant
- Respect customer's decisions
- Follow conversation stages: introduction → qualification → information → persuasion → collection
- Always prioritize customer's best interests
- Do not make false claims or promises
- Handle objections with empathy and facts

Conversation Style:
- Use conversational, natural language
- Ask one question at a time
- Explain why you're asking questions
- Reference previous conversation naturally
- Use customer's name appropriately (not excessively)

Current conversation context and customer profile will be provided in messages.
"""
```

#### 2.2.3 Context Management

```python
class ContextManager:
    """Manages conversation context for LLM"""
    
    MAX_CONTEXT_MESSAGES = 50
    MAX_CONTEXT_TOKENS = 8000  # Conservative estimate
    
    def build_context(
        self,
        session: Session,
        customer_profile: Dict,
        policies: List[Policy]
    ) -> LLMContext:
        """Build context for LLM from session"""
        
        # Get recent messages
        recent_messages = session.message_history[-self.MAX_CONTEXT_MESSAGES:]
        
        # Build message list
        messages = []
        
        # Add summary of earlier conversation if exists
        if session.context_summary:
            messages.append(Message(
                role="system",
                content=f"Earlier conversation summary: {session.context_summary}"
            ))
        
        # Add customer profile
        if customer_profile:
            profile_text = self._format_profile(customer_profile)
            messages.append(Message(
                role="system",
                content=f"Customer profile: {profile_text}"
            ))
        
        # Add available policies (relevant ones only)
        if policies:
            policies_text = self._format_policies(policies)
            messages.append(Message(
                role="system",
                content=f"Available policies: {policies_text}"
            ))
        
        # Add conversation messages
        for msg in recent_messages:
            messages.append(Message(
                role=msg.role,
                content=msg.content
            ))
        
        # Check token count and summarize if needed
        estimated_tokens = self._estimate_tokens(messages)
        if estimated_tokens > self.MAX_CONTEXT_TOKENS:
            messages = self._compress_context(messages)
        
        return LLMContext(messages=messages, metadata={
            "session_id": session.session_id,
            "stage": session.conversation_stage
        })
    
    def _compress_context(self, messages: List[Message]) -> List[Message]:
        """Compress context by summarizing older messages"""
        # Keep system messages and last 30 messages
        # Summarize middle messages
        system_msgs = [m for m in messages if m.role == "system"]
        recent_msgs = messages[-30:]
        
        middle_msgs = messages[len(system_msgs):-30]
        if middle_msgs:
            summary = self._summarize_messages(middle_msgs)
            system_msgs.append(Message(
                role="system",
                content=f"Conversation summary: {summary}"
            ))
        
        return system_msgs + recent_msgs
```

#### 2.2.4 Intent Classification

```python
class Intent(Enum):
    GREETING = "greeting"
    QUESTION = "question"
    OBJECTION = "objection"
    INTEREST = "interest"
    EXIT = "exit"
    INFORMATION_REQUEST = "information_request"
    POLICY_COMPARISON = "policy_comparison"
    UNKNOWN = "unknown"

async def classify_intent(
    provider: LLMProvider,
    message: str,
    context: Dict
) -> Intent:
    """Classify user intent from message"""
    
    # Use LLM for intent classification
    prompt = f"""
Classify the intent of this customer message: "{message}"

Context: {context.get('conversation_stage', 'unknown')}

Possible intents:
- greeting: Customer is greeting or starting conversation
- question: Customer is asking a question
- objection: Customer is raising a concern or objection
- interest: Customer is showing buying interest
- exit: Customer wants to end conversation
- information_request: Customer wants information about policies
- policy_comparison: Customer wants to compare policies

Respond with only the intent name.
"""
    
    response = await provider.generate_response(
        messages=[Message(role="user", content=prompt)],
        config=LLMConfig(temperature=0.1, max_tokens=10)
    )
    
    intent_str = response.content.strip().lower()
    try:
        return Intent(intent_str)
    except ValueError:
        return Intent.UNKNOWN
```

---

### 2.3 Session Manager

#### 2.3.1 Session Data Model

```python
from dataclasses import dataclass
from datetime import datetime
from typing import Dict, List, Optional
from enum import Enum

@dataclass
class CustomerProfile:
    age: Optional[int] = None
    current_coverage: Optional[str] = None
    purpose: Optional[str] = None
    coverage_amount_interest: Optional[str] = None
    dependents: Optional[str] = None
    name: Optional[str] = None

@dataclass
class CollectedData:
    full_name: Optional[str] = None
    phone_number: Optional[str] = None
    nid: Optional[str] = None
    address: Optional[str] = None
    policy_of_interest: Optional[str] = None
    email: Optional[str] = None
    preferred_contact_time: Optional[str] = None
    notes: Optional[str] = None
    
    def is_complete(self) -> bool:
        """Check if all mandatory fields are collected"""
        return all([
            self.full_name,
            self.phone_number,
            self.nid,
            self.address,
            self.policy_of_interest
        ])

@dataclass
class Session:
    session_id: str
    conversation_id: str
    customer_profile: CustomerProfile
    collected_data: CollectedData
    conversation_stage: ConversationStage
    message_history: List[Message]
    context_summary: str
    interest_level: InterestLevel
    created_at: datetime
    last_activity: datetime
    
    @property
    def message_count(self) -> int:
        return len(self.message_history)
    
    @property
    def duration_seconds(self) -> int:
        return (datetime.utcnow() - self.created_at).total_seconds()
```

#### 2.3.2 Session Manager Implementation

```python
import redis
import json
from typing import Optional
import uuid

class SessionManager:
    def __init__(self, redis_client: redis.Redis, ttl: int = 3600):
        self.redis = redis_client
        self.ttl = ttl  # Session TTL in seconds (1 hour default)
    
    async def create_session(self) -> Session:
        """Create new conversation session"""
        session_id = str(uuid.uuid4())
        conversation_id = str(uuid.uuid4())
        
        session = Session(
            session_id=session_id,
            conversation_id=conversation_id,
            customer_profile=CustomerProfile(),
            collected_data=CollectedData(),
            conversation_stage=ConversationStage.INTRODUCTION,
            message_history=[],
            context_summary="",
            interest_level=InterestLevel.NONE,
            created_at=datetime.utcnow(),
            last_activity=datetime.utcnow()
        )
        
        await self.save_session(session)
        return session
    
    async def get_session(self, session_id: str) -> Optional[Session]:
        """Retrieve session by ID"""
        key = f"session:{session_id}"
        data = await self.redis.get(key)
        
        if not data:
            return None
        
        session_dict = json.loads(data)
        return self._deserialize_session(session_dict)
    
    async def save_session(self, session: Session) -> None:
        """Save or update session"""
        key = f"session:{session_id}"
        session_dict = self._serialize_session(session)
        
        await self.redis.setex(
            key,
            self.ttl,
            json.dumps(session_dict)
        )
    
    async def update_session(
        self, 
        session_id: str, 
        updates: Dict
    ) -> Optional[Session]:
        """Update session with partial updates"""
        session = await self.get_session(session_id)
        if not session:
            return None
        
        # Update fields
        for key, value in updates.items():
            if hasattr(session, key):
                setattr(session, key, value)
        
        session.last_activity = datetime.utcnow()
        await self.save_session(session)
        return session
    
    async def delete_session(self, session_id: str) -> None:
        """Delete session"""
        key = f"session:{session_id}"
        await self.redis.delete(key)
    
    def _serialize_session(self, session: Session) -> Dict:
        """Serialize session to dict"""
        return {
            "session_id": session.session_id,
            "conversation_id": session.conversation_id,
            "customer_profile": asdict(session.customer_profile),
            "collected_data": asdict(session.collected_data),
            "conversation_stage": session.conversation_stage.value,
            "message_history": [
                {
                    "role": msg.role.value,
                    "content": msg.content,
                    "timestamp": msg.timestamp.isoformat()
                }
                for msg in session.message_history
            ],
            "context_summary": session.context_summary,
            "interest_level": session.interest_level.value,
            "created_at": session.created_at.isoformat(),
            "last_activity": session.last_activity.isoformat()
        }
    
    def _deserialize_session(self, data: Dict) -> Session:
        """Deserialize dict to Session"""
        # Implementation details...
        pass
```

---

### 2.4 Lead Management Service

#### 2.4.1 Lead Service Implementation

```python
class LeadService:
    def __init__(
        self,
        lead_repository: LeadRepository,
        validation_service: ValidationService,
        conversation_repository: ConversationRepository
    ):
        self.lead_repository = lead_repository
        self.validation_service = validation_service
        self.conversation_repository = conversation_repository
    
    async def create_lead(
        self,
        lead_data: Dict,
        conversation_id: str
    ) -> Lead:
        """Create new lead from collected data"""
        
        # Validate data
        validation_result = await self.validation_service.validate_lead_data(
            lead_data
        )
        
        if not validation_result.is_valid:
            raise ValidationError(validation_result.errors)
        
        # Check for duplicates
        duplicate = await self.lead_repository.find_by_phone_or_nid(
            phone=lead_data.get("phone_number"),
            nid=lead_data.get("nid")
        )
        
        if duplicate:
            # Update existing or create new based on business rules
            # For now, raise error
            raise DuplicateLeadError(f"Lead with phone {lead_data['phone_number']} already exists")
        
        # Create lead
        lead = Lead(
            id=str(uuid.uuid4()),
            full_name=lead_data["full_name"],
            phone_number=lead_data["phone_number"],
            nid=self._encrypt_nid(lead_data["nid"]),  # Encrypt sensitive data
            address=lead_data["address"],
            policy_of_interest=lead_data["policy_of_interest"],
            email=lead_data.get("email"),
            preferred_contact_time=lead_data.get("preferred_contact_time"),
            notes=lead_data.get("notes"),
            conversation_id=conversation_id,
            status=LeadStatus.NEW,
            created_at=datetime.utcnow(),
            updated_at=datetime.utcnow()
        )
        
        # Save to database
        saved_lead = await self.lead_repository.create(lead)
        
        # Optionally save to file (Phase 1)
        await self._save_to_file(saved_lead)
        
        return saved_lead
    
    async def get_leads(
        self,
        filters: Optional[Dict] = None,
        page: int = 1,
        page_size: int = 50
    ) -> PaginatedResult[Lead]:
        """Retrieve leads with filtering and pagination"""
        
        return await self.lead_repository.find_all(
            filters=filters or {},
            page=page,
            page_size=page_size
        )
    
    async def export_leads(
        self,
        format: str,  # "csv", "json", "xlsx"
        filters: Optional[Dict] = None
    ) -> bytes:
        """Export leads to specified format"""
        
        # Get all leads matching filters
        leads = await self.lead_repository.find_all(
            filters=filters or {},
            page_size=10000  # Large limit for export
        )
        
        # Convert to export format
        if format == "csv":
            return self._export_to_csv(leads.items)
        elif format == "json":
            return self._export_to_json(leads.items)
        elif format == "xlsx":
            return self._export_to_excel(leads.items)
        else:
            raise ValueError(f"Unsupported format: {format}")
    
    def _encrypt_nid(self, nid: str) -> str:
        """Encrypt NID before storage"""
        # Use AES encryption
        # Implementation using cryptography library
        pass
    
    async def _save_to_file(self, lead: Lead) -> None:
        """Save lead to text file (Phase 1 option)"""
        # Append to daily JSON file
        filename = f"leads_{datetime.utcnow().strftime('%Y-%m-%d')}.json"
        # Implementation...
        pass
```

---

### 2.5 Policy Management Service

#### 2.5.1 Policy Matching Algorithm

```python
class PolicyService:
    def __init__(self, policy_repository: PolicyRepository):
        self.policy_repository = policy_repository
    
    async def get_relevant_policies(
        self,
        customer_profile: CustomerProfile
    ) -> List[Policy]:
        """Get policies relevant to customer profile"""
        
        # Get all active policies
        all_policies = await self.policy_repository.find_all(active=True)
        
        # Score and rank policies
        scored_policies = []
        
        for policy in all_policies:
            score = self._calculate_relevance_score(policy, customer_profile)
            if score > 0:  # Only include policies with positive score
                scored_policies.append((policy, score))
        
        # Sort by score (highest first)
        scored_policies.sort(key=lambda x: x[1], reverse=True)
        
        # Return top 3-5 policies
        return [p[0] for p in scored_policies[:5]]
    
    def _calculate_relevance_score(
        self,
        policy: Policy,
        profile: CustomerProfile
    ) -> float:
        """Calculate how relevant a policy is to customer"""
        
        score = 0.0
        
        # Age eligibility check
        if profile.age:
            age_req = policy.age_requirements
            if age_req.min_age <= profile.age <= age_req.max_age:
                score += 10.0
            else:
                return 0.0  # Not eligible
        
        # Purpose matching
        if profile.purpose:
            purpose_keywords = {
                "family protection": ["family", "children", "spouse"],
                "debt coverage": ["mortgage", "loan", "debt"],
                "estate planning": ["estate", "inheritance", "wealth"],
                "business": ["business", "partnership"]
            }
            
            for keyword in purpose_keywords.get(profile.purpose, []):
                if keyword in policy.description.lower():
                    score += 5.0
        
        # Coverage amount matching
        if profile.coverage_amount_interest:
            # Parse customer's interest range and match to policy range
            score += 3.0
        
        # Company policy boost
        if policy.company == "own_company":
            score += 2.0
        
        return score
```

---

## Data Structures and Models

### 3.1 Message Model

```python
from dataclasses import dataclass
from datetime import datetime
from enum import Enum
from typing import Optional, Dict

class MessageRole(Enum):
    USER = "user"
    ASSISTANT = "assistant"
    SYSTEM = "system"

@dataclass
class Message:
    id: str
    conversation_id: str
    role: MessageRole
    content: str
    timestamp: datetime
    metadata: Optional[Dict] = None
```

### 3.2 Lead Model

```python
from dataclasses import dataclass
from datetime import datetime
from enum import Enum

class LeadStatus(Enum):
    NEW = "new"
    CONTACTED = "contacted"
    CONVERTED = "converted"
    NOT_INTERESTED = "not_interested"

@dataclass
class Lead:
    id: str
    full_name: str
    phone_number: str  # Encrypted in DB
    nid: str  # Encrypted in DB
    address: str
    policy_of_interest: str
    email: Optional[str] = None
    preferred_contact_time: Optional[str] = None
    notes: Optional[str] = None
    conversation_id: str
    status: LeadStatus
    created_at: datetime
    updated_at: datetime
```

### 3.3 Policy Model

```python
from dataclasses import dataclass
from datetime import datetime
from typing import List, Dict

@dataclass
class CoverageRange:
    min: float
    max: float

@dataclass
class PremiumRange:
    min_monthly: float
    max_monthly: float
    factors: List[str]  # Age, health, coverage amount, etc.

@dataclass
class AgeRequirements:
    min_age: int
    max_age: int

@dataclass
class Policy:
    id: str
    name: str
    company: str  # "own_company" or competitor name
    type: str  # "term", "whole_life", "universal"
    coverage_range: CoverageRange
    premium_range: PremiumRange
    age_requirements: AgeRequirements
    benefits: List[str]
    features: List[str]
    description: str
    medical_exam_required: bool
    created_at: datetime
    updated_at: datetime
    active: bool = True
```

---

## API Specifications

### 4.1 Conversation API

#### 4.1.1 Start Conversation

**Endpoint**: `POST /api/conversation/start`

**Request**:
```json
{
  "source": "web" // Optional
}
```

**Response**:
```json
{
  "session_id": "uuid",
  "conversation_id": "uuid",
  "welcome_message": "Hello! I'm [Agent Name]...",
  "status": "started"
}
```

**Implementation**:
```python
@router.post("/conversation/start")
async def start_conversation(request: StartConversationRequest):
    session = await conversation_service.start_conversation()
    welcome_message = await conversation_service.get_welcome_message()
    
    return StartConversationResponse(
        session_id=session.session_id,
        conversation_id=session.conversation_id,
        welcome_message=welcome_message,
        status="started"
    )
```

#### 4.1.2 Send Message

**Endpoint**: `POST /api/conversation/message`

**Request**:
```json
{
  "session_id": "uuid",
  "message": "I'm interested in life insurance"
}
```

**Response**:
```json
{
  "session_id": "uuid",
  "response": "Great! I'd be happy to help...",
  "interest_detected": "medium",
  "conversation_stage": "qualification",
  "metadata": {
    "message_count": 5,
    "extracted_data": {
      "age": 35
    }
  }
}
```

#### 4.1.3 Get Conversation History

**Endpoint**: `GET /api/conversation/{session_id}`

**Response**:
```json
{
  "session_id": "uuid",
  "conversation_id": "uuid",
  "messages": [
    {
      "role": "user",
      "content": "Hello",
      "timestamp": "2024-01-15T10:00:00Z"
    },
    {
      "role": "assistant",
      "content": "Hello! How can I help...",
      "timestamp": "2024-01-15T10:00:01Z"
    }
  ],
  "customer_profile": {
    "age": 35,
    "name": "John"
  },
  "conversation_stage": "information"
}
```

### 4.2 Lead Management API

#### 4.2.1 Create Lead

**Endpoint**: `POST /api/leads`

**Request**:
```json
{
  "full_name": "John Doe",
  "phone_number": "+1234567890",
  "nid": "123456789",
  "address": "123 Main St, City, Country",
  "policy_of_interest": "term-life-20yr",
  "email": "john@example.com",
  "preferred_contact_time": "evening",
  "conversation_id": "uuid"
}
```

**Response**:
```json
{
  "id": "uuid",
  "status": "created",
  "message": "Thank you! Your information has been saved..."
}
```

#### 4.2.2 List Leads

**Endpoint**: `GET /api/leads`

**Query Parameters**:
- `status`: Filter by status
- `policy`: Filter by policy
- `date_from`: Filter from date
- `date_to`: Filter to date
- `page`: Page number
- `page_size`: Items per page

**Response**:
```json
{
  "leads": [
    {
      "id": "uuid",
      "full_name": "John Doe",
      "phone_number": "***-***-7890", // Masked
      "policy_of_interest": "term-life-20yr",
      "status": "new",
      "created_at": "2024-01-15T10:00:00Z"
    }
  ],
  "pagination": {
    "page": 1,
    "page_size": 50,
    "total": 150,
    "total_pages": 3
  }
}
```

---

## Database Schema Design

### 5.1 Lead Table

```sql
CREATE TABLE leads (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,  -- Encrypted
    nid VARCHAR(100) NOT NULL,  -- Encrypted
    address TEXT NOT NULL,
    policy_of_interest VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    preferred_contact_time VARCHAR(50),
    notes TEXT,
    conversation_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'new',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_conversation 
        FOREIGN KEY (conversation_id) 
        REFERENCES conversations(id)
);

CREATE INDEX idx_leads_phone ON leads(phone_number);
CREATE INDEX idx_leads_status ON leads(status);
CREATE INDEX idx_leads_created_at ON leads(created_at);
CREATE INDEX idx_leads_conversation_id ON leads(conversation_id);
```

### 5.2 Conversation Table

```sql
CREATE TABLE conversations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id VARCHAR(255) UNIQUE NOT NULL,
    customer_interests TEXT[],  -- Array of strings
    detected_intent VARCHAR(50),
    conversation_summary TEXT,
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP,
    duration_seconds INTEGER,
    
    CONSTRAINT chk_duration 
        CHECK (duration_seconds >= 0)
);

CREATE INDEX idx_conversations_session_id ON conversations(session_id);
CREATE INDEX idx_conversations_started_at ON conversations(started_at);
```

### 5.3 Message Table

```sql
CREATE TABLE messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conversation_id UUID NOT NULL,
    role VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metadata JSONB,
    
    CONSTRAINT fk_conversation 
        FOREIGN KEY (conversation_id) 
        REFERENCES conversations(id) ON DELETE CASCADE,
    CONSTRAINT chk_role 
        CHECK (role IN ('user', 'assistant', 'system'))
);

CREATE INDEX idx_messages_conversation_id ON messages(conversation_id);
CREATE INDEX idx_messages_timestamp ON messages(timestamp);
```

### 5.4 Policy Table

```sql
CREATE TABLE policies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    company VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    coverage_amount_range JSONB NOT NULL,
    premium_range JSONB NOT NULL,
    age_requirements JSONB NOT NULL,
    benefits TEXT[] NOT NULL,
    features TEXT[] NOT NULL,
    description TEXT,
    medical_exam_required BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_policies_company ON policies(company);
CREATE INDEX idx_policies_active ON policies(active);
CREATE INDEX idx_policies_type ON policies(type);
```

---

## Algorithms and Logic

### 6.1 Information Extraction Algorithm

```python
async def extract_information(
    message: str,
    context: Dict,
    entity_types: List[str] = ["age", "phone", "name", "address", "email"]
) -> Dict[str, Any]:
    """Extract structured information from natural language"""
    
    extracted = {}
    
    # Use LLM for entity extraction
    prompt = f"""
Extract the following information from this message: "{message}"

Context: {context}

Extract:
- age: Numeric age (18-100)
- phone: Phone number with country code
- name: Full name
- address: Complete address
- email: Email address

Return JSON with extracted values or null if not found.
"""
    
    response = await llm_provider.generate_response(
        messages=[Message(role="user", content=prompt)],
        config=LLMConfig(temperature=0.1, max_tokens=200)
    )
    
    try:
        extracted = json.loads(response.content)
    except:
        # Fallback to regex patterns
        extracted = extract_with_regex(message, entity_types)
    
    return extracted

def extract_with_regex(message: str, entity_types: List[str]) -> Dict:
    """Fallback regex-based extraction"""
    extracted = {}
    
    # Age extraction
    age_pattern = r'\b(\d{2})\s*(?:years?\s*old|age|aged)|\b(?:age|aged)\s*(\d{2})\b'
    age_match = re.search(age_pattern, message.lower())
    if age_match:
        age = int(age_match.group(1) or age_match.group(2))
        if 18 <= age <= 100:
            extracted["age"] = age
    
    # Phone extraction
    phone_pattern = r'\+?\d{1,3}[-.\s]?\(?\d{1,4}\)?[-.\s]?\d{1,4}[-.\s]?\d{1,9}'
    phone_match = re.search(phone_pattern, message)
    if phone_match:
        extracted["phone"] = phone_match.group(0)
    
    # Email extraction
    email_pattern = r'\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b'
    email_match = re.search(email_pattern, message)
    if email_match:
        extracted["email"] = email_match.group(0)
    
    return extracted
```

### 6.2 Exit Signal Detection

```python
def is_exit_signal(message: str, intent: Intent) -> bool:
    """Detect if message indicates customer wants to exit"""
    
    exit_keywords = [
        "not interested", "no thanks", "I'll pass", 
        "I don't want", "maybe later", "I have to go",
        "thanks but no", "not for me"
    ]
    
    message_lower = message.lower()
    
    # Explicit exit keywords
    for keyword in exit_keywords:
        if keyword in message_lower:
            return True
    
    # Intent-based detection
    if intent == Intent.EXIT:
        return True
    
    # Pattern: "Thanks but no thanks", "Not interested, bye"
    if any(pattern in message_lower for pattern in ["thanks but", "bye", "see you"]):
        if any(negative in message_lower for negative in ["not", "no", "don't"]):
            return True
    
    return False
```

---

## State Management

### 7.1 Conversation State Machine

```python
from enum import Enum

class ConversationStage(Enum):
    INTRODUCTION = "introduction"
    QUALIFICATION = "qualification"
    INFORMATION = "information"
    PERSUASION = "persuasion"
    OBJECTION_HANDLING = "objection_handling"
    INFORMATION_COLLECTION = "information_collection"
    CLOSING = "closing"
    ENDED = "ended"

# State transitions
STATE_TRANSITIONS = {
    ConversationStage.INTRODUCTION: [
        ConversationStage.QUALIFICATION,
        ConversationStage.ENDED
    ],
    ConversationStage.QUALIFICATION: [
        ConversationStage.INFORMATION,
        ConversationStage.OBJECTION_HANDLING,
        ConversationStage.ENDED
    ],
    ConversationStage.INFORMATION: [
        ConversationStage.PERSUASION,
        ConversationStage.OBJECTION_HANDLING,
        ConversationStage.INFORMATION_COLLECTION,
        ConversationStage.ENDED
    ],
    ConversationStage.PERSUASION: [
        ConversationStage.INFORMATION_COLLECTION,
        ConversationStage.OBJECTION_HANDLING,
        ConversationStage.ENDED
    ],
    ConversationStage.OBJECTION_HANDLING: [
        ConversationStage.INFORMATION,
        ConversationStage.PERSUASION,
        ConversationStage.ENDED
    ],
    ConversationStage.INFORMATION_COLLECTION: [
        ConversationStage.CLOSING,
        ConversationStage.ENDED
    ],
    ConversationStage.CLOSING: [
        ConversationStage.ENDED
    ],
    ConversationStage.ENDED: []  # Terminal state
}

def can_transition(
    current: ConversationStage,
    target: ConversationStage
) -> bool:
    """Check if state transition is valid"""
    return target in STATE_TRANSITIONS.get(current, [])
```

---

## Error Handling

### 8.1 Error Types

```python
class ApplicationError(Exception):
    """Base application error"""
    pass

class ValidationError(ApplicationError):
    """Data validation error"""
    pass

class SessionNotFoundError(ApplicationError):
    """Session not found"""
    pass

class LLMAPIError(ApplicationError):
    """LLM API error"""
    pass

class LLMRateLimitError(LLMAPIError):
    """LLM rate limit exceeded"""
    pass

class DuplicateLeadError(ApplicationError):
    """Duplicate lead detected"""
    pass
```

### 8.2 Error Handling Middleware

```python
from fastapi import Request, status
from fastapi.responses import JSONResponse

@app.exception_handler(ValidationError)
async def validation_error_handler(request: Request, exc: ValidationError):
    return JSONResponse(
        status_code=status.HTTP_400_BAD_REQUEST,
        content={
            "error": "validation_error",
            "message": str(exc),
            "details": exc.errors if hasattr(exc, 'errors') else None
        }
    )

@app.exception_handler(LLMAPIError)
async def llm_error_handler(request: Request, exc: LLMAPIError):
    return JSONResponse(
        status_code=status.HTTP_503_SERVICE_UNAVAILABLE,
        content={
            "error": "llm_service_error",
            "message": "I'm having a technical issue. Please try again in a moment.",
            "retry_after": 5
        }
    )
```

---

## Security Implementation

### 9.1 Data Encryption

```python
from cryptography.fernet import Fernet
import os

class EncryptionService:
    def __init__(self):
        key = os.getenv("ENCRYPTION_KEY")
        if not key:
            raise ValueError("ENCRYPTION_KEY environment variable not set")
        self.cipher = Fernet(key.encode())
    
    def encrypt(self, plaintext: str) -> str:
        """Encrypt sensitive data"""
        return self.cipher.encrypt(plaintext.encode()).decode()
    
    def decrypt(self, ciphertext: str) -> str:
        """Decrypt sensitive data"""
        return self.cipher.decrypt(ciphertext.encode()).decode()
```

### 9.2 Authentication (Admin)

```python
from jose import JWTError, jwt
from datetime import datetime, timedelta

class AuthService:
    SECRET_KEY = os.getenv("JWT_SECRET_KEY")
    ALGORITHM = "HS256"
    ACCESS_TOKEN_EXPIRE_MINUTES = 30
    
    def create_access_token(self, username: str) -> str:
        """Create JWT token"""
        expire = datetime.utcnow() + timedelta(
            minutes=self.ACCESS_TOKEN_EXPIRE_MINUTES
        )
        to_encode = {
            "sub": username,
            "exp": expire
        }
        return jwt.encode(to_encode, self.SECRET_KEY, algorithm=self.ALGORITHM)
    
    def verify_token(self, token: str) -> Optional[str]:
        """Verify JWT token"""
        try:
            payload = jwt.decode(
                token, 
                self.SECRET_KEY, 
                algorithms=[self.ALGORITHM]
            )
            return payload.get("sub")
        except JWTError:
            return None
```

---

## LLM Integration Details

### 10.1 System Prompt Template

```python
SYSTEM_PROMPT_TEMPLATE = """
You are an AI life insurance sales agent for {company_name}.
Your name is {agent_name}.

Your role is to:
1. Help customers understand life insurance options
2. Provide accurate information about policies
3. Build rapport and trust
4. Identify interested prospects
5. Collect information from interested customers

Guidelines:
- Be professional, friendly, and empathetic
- Use persuasive techniques naturally (not aggressive)
- Be transparent that you're an AI assistant
- Respect customer's decisions
- Follow conversation stages: introduction → qualification → information → persuasion → collection

Current conversation stage: {stage}
Customer profile: {customer_profile}

Available policies:
{policies}

Remember:
- Ask one question at a time
- Explain why you're asking questions
- Reference previous conversation naturally
- Use customer's name appropriately (not excessively)
- Handle objections with empathy and facts
"""

def build_system_prompt(
    company_name: str,
    agent_name: str,
    stage: ConversationStage,
    customer_profile: CustomerProfile,
    policies: List[Policy]
) -> str:
    """Build system prompt from template"""
    return SYSTEM_PROMPT_TEMPLATE.format(
        company_name=company_name,
        agent_name=agent_name,
        stage=stage.value,
        customer_profile=format_profile(customer_profile),
        policies=format_policies(policies)
    )
```

### 10.2 Response Filtering

```python
def filter_response(response: str) -> str:
    """Filter LLM response for safety and compliance"""
    
    # Remove any potential harmful content
    blocked_phrases = [
        "guaranteed approval",  # No guarantees
        "must buy",  # Too pushy
        "limited time offer"  # Unless verified
    ]
    
    filtered = response
    for phrase in blocked_phrases:
        if phrase.lower() in filtered.lower():
            # Replace or remove
            filtered = filtered.replace(phrase, "")
    
    # Ensure transparency about AI nature if needed
    if "I am" in filtered and "human" in filtered.lower():
        # Correct if LLM claims to be human
        filtered = filtered.replace("I am human", "I am an AI assistant")
    
    return filtered.strip()
```

---

## Validation Logic

### 11.1 Phone Number Validation

```python
import re
import phonenumbers

def validate_phone_number(phone: str) -> ValidationResult:
    """Validate phone number format"""
    
    try:
        # Try parsing with phonenumbers library
        parsed = phonenumbers.parse(phone, None)
        if phonenumbers.is_valid_number(parsed):
            return ValidationResult(
                is_valid=True,
                normalized=phonenumbers.format_number(
                    parsed, 
                    phonenumbers.PhoneNumberFormat.E164
                )
            )
        else:
            return ValidationResult(
                is_valid=False,
                errors=["Invalid phone number format"]
            )
    except:
        # Fallback to regex
        pattern = r'^\+?[1-9]\d{1,14}$'
        if re.match(pattern, phone.replace(" ", "").replace("-", "")):
            return ValidationResult(is_valid=True, normalized=phone)
        else:
            return ValidationResult(
                is_valid=False,
                errors=["Phone number must include country code. Example: +1234567890"]
            )
```

### 11.2 NID Validation

```python
def validate_nid(nid: str, country: str = "default") -> ValidationResult:
    """Validate National ID format (country-specific)"""
    
    # Remove spaces and hyphens
    cleaned = nid.replace(" ", "").replace("-", "")
    
    # Country-specific validation
    if country == "US":
        # SSN format: 9 digits
        if len(cleaned) == 9 and cleaned.isdigit():
            return ValidationResult(is_valid=True, normalized=cleaned)
    
    elif country == "BD":
        # Bangladesh NID: 10 or 13 digits
        if len(cleaned) in [10, 13] and cleaned.isdigit():
            return ValidationResult(is_valid=True, normalized=cleaned)
    
    # Default: alphanumeric, 8-20 characters
    if 8 <= len(cleaned) <= 20:
        return ValidationResult(is_valid=True, normalized=cleaned)
    
    return ValidationResult(
        is_valid=False,
        errors=["Invalid NID format. Please provide a valid National ID."]
    )
```

---

## Performance Optimizations

### 12.1 Caching Strategy

```python
from functools import lru_cache
import redis

class PolicyCache:
    def __init__(self, redis_client: redis.Redis):
        self.redis = redis_client
        self.ttl = 3600  # 1 hour
    
    async def get_policies(self, cache_key: str) -> Optional[List[Policy]]:
        """Get policies from cache"""
        cached = await self.redis.get(f"policies:{cache_key}")
        if cached:
            return json.loads(cached)
        return None
    
    async def set_policies(
        self, 
        cache_key: str, 
        policies: List[Policy]
    ) -> None:
        """Cache policies"""
        await self.redis.setex(
            f"policies:{cache_key}",
            self.ttl,
            json.dumps([asdict(p) for p in policies])
        )
```

### 12.2 Database Query Optimization

```python
# Use indexes
# Use select_related for foreign keys
# Use pagination for large datasets
# Use connection pooling

# Example optimized query
async def get_leads_optimized(
    filters: Dict,
    page: int,
    page_size: int
) -> PaginatedResult:
    """Optimized lead query with pagination"""
    
    query = select(Lead).where(Lead.active == True)
    
    # Apply filters (using indexes)
    if filters.get("status"):
        query = query.where(Lead.status == filters["status"])
    
    # Count total (separate query for efficiency)
    count_query = select(func.count()).select_from(query.subquery())
    total = await db.execute(count_query)
    
    # Paginated results
    query = query.order_by(Lead.created_at.desc())
    query = query.limit(page_size).offset((page - 1) * page_size)
    
    results = await db.execute(query)
    leads = results.scalars().all()
    
    return PaginatedResult(
        items=leads,
        total=total,
        page=page,
        page_size=page_size
    )
```

---

## Appendix

### A.1 Code Organization

```
src/
├── api/
│   ├── routes/
│   │   ├── conversation.py
│   │   ├── leads.py
│   │   └── policies.py
│   └── middleware/
│       ├── auth.py
│       └── error_handler.py
├── services/
│   ├── conversation_service.py
│   ├── lead_service.py
│   ├── policy_service.py
│   └── validation_service.py
├── llm/
│   ├── providers/
│   │   ├── base.py
│   │   ├── openai.py
│   │   └── anthropic.py
│   ├── context_manager.py
│   └── prompt_manager.py
├── models/
│   ├── lead.py
│   ├── conversation.py
│   └── policy.py
├── repositories/
│   ├── lead_repository.py
│   ├── conversation_repository.py
│   └── policy_repository.py
└── utils/
    ├── encryption.py
    └── validators.py
```

### A.2 Testing Strategy

- Unit tests for each service
- Integration tests for API endpoints
- Mock LLM responses for testing
- Test data fixtures
- Performance tests for critical paths

---

**Document Status**: Draft - Pending Review  
**Next Steps**: Implementation begins after review and approval

