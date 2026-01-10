export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  fullName: string;
  membershipPlanId?: string;
  createdAt: string;
  active: boolean;
}

export interface Account {
  id: string;
  name: string;
  accountType: string;
  initialBalance: number;
  currentBalance: number;
  currency: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface Transaction {
  id: string;
  accountId: string;
  userId: string;
  amount: number;
  transactionType: string;
  categoryId?: string;
  description?: string;
  transactionDate: string;
  tags?: string[];
  createdAt: string;
  updatedAt: string;
}

export interface Transfer {
  id: string;
  fromAccountId: string;
  toAccountId: string;
  amount: number;
  description?: string;
  transactionDate: string;
  tags?: string[];
  fromTransaction: Transaction;
  toTransaction: Transaction;
  createdAt: string;
}

export interface Dashboard {
  totalBalance: number;
  monthlyIncome: number;
  monthlyExpenses: number;
  monthlyBalance: number;
  monthlyTransactionCount: number;
  monthStart: string;
  monthEnd: string;
  accounts: AccountSummary[];
  recentTransactions: Transaction[];
}

export interface AccountSummary {
  id: string;
  name: string;
  accountType: string;
  currentBalance: number;
  currency: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  user: User;
}

export interface Category {
  id: string;
  userId?: string;
  name: string;
  code?: string;
  type: string; // 'INCOME' | 'EXPENSE'
  isSystem: boolean;
  parentCategoryId?: string;
  icon?: string;
  color?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateCategoryRequest {
  name: string;
  code?: string;
  type: 'INCOME' | 'EXPENSE';
  icon?: string;
  color?: string;
}

