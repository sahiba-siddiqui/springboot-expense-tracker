package com.example.expensetracker.service;

import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

        @Service
        public class ExpenseService {

            private final ExpenseRepository expenseRepository;

            public ExpenseService(ExpenseRepository expenseRepository) {
                this.expenseRepository = expenseRepository;
            }

            // Get all expenses
            public List<Expense> getAllExpenses() {
                return expenseRepository.findAll();
            }

            // Get expense by id
            public Expense getExpenseById(Long id) {
                return expenseRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
            }

            // Add new expense
            public Expense addExpense(Expense expense) {
                return expenseRepository.save(expense);
            }

            // Update existing expense
            public Expense updateExpense(Long id, Expense expenseDetails) {
                Expense expense = expenseRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

                expense.setTitle(expenseDetails.getTitle());
                expense.setCategory(expenseDetails.getCategory());
                expense.setAmount(expenseDetails.getAmount());
                expense.setDate(expenseDetails.getDate());

                return expenseRepository.save(expense);
            }

            // Delete expense
            public void deleteExpense(Long id) {
                if (!expenseRepository.existsById(id)) {
                    throw new ResourceNotFoundException("Cannot delete. Expense not found with id: " + id);
                }
                expenseRepository.deleteById(id);
            }


            public List<Expense> getExpensesByCategory(String category) {
                return expenseRepository.findByCategoryIgnoreCase(category);
            }

            public double getMonthlyTotal(int year, int month) {
                return expenseRepository.findAll().stream()
                        .filter(e -> e.getDate() != null &&
                                e.getDate().getYear() == year &&
                                e.getDate().getMonthValue() == month)
                        .mapToDouble(Expense::getAmount)
                        .sum();
            }
            public List<Expense> getByDateRange(LocalDate from, LocalDate to) {
                return expenseRepository.findByDateBetween(from, to);
            }
        }
