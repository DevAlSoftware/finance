export interface User {
  id: string;
  email: string;
  name: string;
  createdAt: string;
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
  name: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

