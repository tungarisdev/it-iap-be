package com.example.it_iap.service;

import com.example.it_iap.dto.ai.response.AIInteractive;
import com.example.it_iap.dto.interview.FeedbackForQuestion;
import com.example.it_iap.dto.question.request.AICreateQuestionRequest;
import com.example.it_iap.entity.InterviewQuestion;
import com.example.it_iap.entity.Json.OverallResult;
import com.example.it_iap.entity.PromptVersion;
import com.example.it_iap.entity.enums.TargetLevel;
import com.example.it_iap.entity.enums.TargetPosition;

import java.util.List;

public interface AIService {
    List<AICreateQuestionRequest> generateQuestion (int quantity, TargetLevel level, TargetPosition position, PromptVersion promptVersion);
    OverallResult generateFeedback (List<FeedbackForQuestion> feedbackForQuestions, PromptVersion promptVersion);
    AIInteractive interactiveWithAi (InterviewQuestion interviewQuestion, String userAnswer);
}
