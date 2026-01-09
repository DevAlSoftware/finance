import axios from 'axios';
import type { AxiosInstance, AxiosError } from 'axios';
import type { 
  LoginRequest, 
  RegisterRequest, 
  AuthResponse,
  Account,
  Transaction,
  Transfer,
  Dashboard
} from '../types';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8888/api';

class ApiService {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    this.client.interceptors.request.use((config) => {
      const token = localStorage.getItem('token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    });

    this.client.interceptors.response.use(
      (response) => response,
      (error: AxiosError) => {
        if (error.response?.status === 401) {
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  async login(data: LoginRequest): Promise<AuthResponse> {
    const response = await this.client.post<AuthResponse>('/auth/login', data);
    return response.data;
  }

  async register(data: RegisterRequest): Promise<AuthResponse> {
    const response = await this.client.post<AuthResponse>('/auth/register', data);
    return response.data;
  }

  async getAccounts(): Promise<Account[]> {
    const response = await this.client.get<Account[]>('/accounts');
    return response.data;
  }

  async getAccountById(id: string): Promise<Account> {
    const response = await this.client.get<Account>(`/accounts/${id}`);
    return response.data;
  }

  async createAccount(data: Partial<Account>): Promise<Account> {
    const response = await this.client.post<Account>('/accounts', data);
    return response.data;
  }

  async updateAccount(id: string, data: Partial<Account>): Promise<Account> {
    const response = await this.client.put<Account>(`/accounts/${id}`, data);
    return response.data;
  }

  async deleteAccount(id: string): Promise<void> {
    await this.client.delete(`/accounts/${id}`);
  }

  async getTransactions(startDate?: string, endDate?: string): Promise<Transaction[]> {
    const params = new URLSearchParams();
    if (startDate) params.append('startDate', startDate);
    if (endDate) params.append('endDate', endDate);
    
    const response = await this.client.get<Transaction[]>(
      `/transactions?${params.toString()}`
    );
    return response.data;
  }

  async createTransaction(data: Partial<Transaction>): Promise<Transaction> {
    const response = await this.client.post<Transaction>('/transactions', data);
    return response.data;
  }

  async updateTransaction(id: string, data: Partial<Transaction>): Promise<Transaction> {
    const response = await this.client.put<Transaction>(`/transactions/${id}`, data);
    return response.data;
  }

  async deleteTransaction(id: string): Promise<void> {
    await this.client.delete(`/transactions/${id}`);
  }

  async createTransfer(data: Partial<Transfer>): Promise<Transfer> {
    const response = await this.client.post<Transfer>('/transactions/transfer', data);
    return response.data;
  }

  async getDashboard(): Promise<Dashboard> {
    const response = await this.client.get<Dashboard>('/dashboard');
    return response.data;
  }
}

export const apiService = new ApiService();

