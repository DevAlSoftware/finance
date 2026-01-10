import { useEffect, useState } from 'react';
import { apiService } from '../services/api';
import { Card, Button, Loading, Input, Select } from '../components';
import { useLanguage } from '../hooks/useLanguage';
import type { Category, CreateCategoryRequest } from '../types';

// Lista de emojis/iconos comunes para categorías
const CATEGORY_ICONS = [
  '📁', '💰', '💳', '🛒', '🍔', '🚗', '🏠', '⚡', '💧', '📱',
  '💊', '🎓', '🎬', '🏋️', '✈️', '🏥', '🎁', '🎨', '📚', '🎮',
  '☕', '🍕', '🍎', '👕', '💻', '📺', '🔧', '🌳', '🎵', '🎯'
];

// Colores predefinidos para categorías
const CATEGORY_COLORS = [
  '#EF4444', '#F59E0B', '#10B981', '#3B82F6', '#8B5CF6',
  '#EC4899', '#14B8A6', '#F97316', '#84CC16', '#06B6D4',
  '#6366F1', '#A855F7', '#F43F5E', '#64748B', '#6B7280'
];

export const Categories: React.FC = () => {
  const { t } = useLanguage();
  const [categories, setCategories] = useState<Category[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [formData, setFormData] = useState<CreateCategoryRequest>({
    name: '',
    code: '',
    type: 'EXPENSE',
    icon: '📁',
    color: '#6B7280',
  });
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showIconPicker, setShowIconPicker] = useState(false);
  const [showColorPicker, setShowColorPicker] = useState(false);

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      setIsLoading(true);
      const data = await apiService.getCategories();
      setCategories(data);
    } catch (err) {
      console.error('Error loading categories:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }));
    }
  };

  const handleIconSelect = (icon: string) => {
    setFormData((prev) => ({ ...prev, icon }));
    setShowIconPicker(false);
  };

  const handleColorSelect = (color: string) => {
    setFormData((prev) => ({ ...prev, color }));
    setShowColorPicker(false);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrors({});
    setIsSubmitting(true);

    try {
      await apiService.createCategory({
        name: formData.name,
        code: formData.code || undefined,
        type: formData.type,
        icon: formData.icon,
        color: formData.color,
      });
      setFormData({ name: '', code: '', type: 'EXPENSE', icon: '📁', color: '#6B7280' });
      setShowCreateForm(false);
      await loadCategories();
    } catch (err: any) {
      if (err.response?.data?.message) {
        setErrors({ general: err.response.data.message });
      } else {
        setErrors({ general: t('categories.errorCreating') });
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDelete = async (id: string, isSystem: boolean) => {
    if (isSystem) {
      alert(t('categories.cannotDeleteSystem'));
      return;
    }

    if (!confirm(t('categories.deleteConfirm'))) {
      return;
    }

    try {
      await apiService.deleteCategory(id);
      await loadCategories();
    } catch (err: any) {
      alert(err.response?.data?.message || t('categories.errorDeleting'));
    }
  };

  // Filtrar solo categorías personalizadas (no del sistema)
  const userCategories = categories.filter(cat => !cat.isSystem);

  if (isLoading) {
    return <Loading fullScreen />;
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-spartan-900">{t('categories.title')}</h1>
          <p className="text-spartan-600 mt-1">{t('categories.subtitle')}</p>
        </div>
        <Button
          variant="primary"
          onClick={() => setShowCreateForm(!showCreateForm)}
        >
          {showCreateForm ? t('common.cancel') : `+ ${t('categories.newCategory')}`}
        </Button>
      </div>

      {showCreateForm && (
        <Card>
          <form onSubmit={handleSubmit} className="space-y-4">
            {errors.general && (
              <div className="bg-red-50 border-2 border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">
                {errors.general}
              </div>
            )}

            <div>
              <Select
                label={t('categories.type')}
                name="type"
                value={formData.type}
                onChange={handleChange}
                error={errors.type}
                required
                options={[
                  { value: 'INCOME', label: t('categories.forIncome') },
                  { value: 'EXPENSE', label: t('categories.forExpense') },
                ]}
              />
              <p className="text-xs text-spartan-600 mt-1">{t('categories.typeHelp')}</p>
            </div>

            <Input
              label={t('categories.name')}
              name="name"
              value={formData.name}
              onChange={handleChange}
              error={errors.name}
              required
              placeholder={t('categories.namePlaceholder')}
            />

            <Input
              label={`${t('categories.code')} (${t('common.optional')})`}
              name="code"
              value={formData.code}
              onChange={handleChange}
              placeholder={t('categories.codePlaceholder')}
            />

            {/* Selector de Icono */}
            <div className="relative">
              <label className="block text-sm font-medium text-spartan-700 mb-1">
                Icono {t('common.optional')}
              </label>
              <div className="flex items-center gap-3">
                <button
                  type="button"
                  onClick={() => setShowIconPicker(!showIconPicker)}
                  className="w-16 h-16 rounded-lg border-2 border-spartan-300 bg-white flex items-center justify-center text-2xl hover:border-spartan-500 transition-colors"
                  style={{ backgroundColor: formData.color ? `${formData.color}20` : undefined }}
                >
                  {formData.icon || '📁'}
                </button>
                <Input
                  name="icon"
                  value={formData.icon || ''}
                  onChange={(e) => setFormData(prev => ({ ...prev, icon: e.target.value }))}
                  placeholder="Ej: 📁, 💰, 🍔"
                  className="flex-1"
                />
              </div>
              {showIconPicker && (
                <div className="absolute z-10 mt-2 p-4 bg-white border-2 border-spartan-300 rounded-lg shadow-lg max-h-64 overflow-y-auto grid grid-cols-6 gap-2">
                  {CATEGORY_ICONS.map((icon) => (
                    <button
                      key={icon}
                      type="button"
                      onClick={() => handleIconSelect(icon)}
                      className="w-10 h-10 rounded-lg border-2 border-spartan-200 hover:border-spartan-500 flex items-center justify-center text-xl transition-colors"
                      style={{ backgroundColor: formData.color ? `${formData.color}20` : undefined }}
                    >
                      {icon}
                    </button>
                  ))}
                </div>
              )}
              <p className="text-xs text-spartan-600 mt-1">Escribe un emoji o selecciona uno de la lista</p>
            </div>

            {/* Selector de Color */}
            <div className="relative">
              <label className="block text-sm font-medium text-spartan-700 mb-1">
                Color {t('common.optional')}
              </label>
              <div className="flex items-center gap-3">
                <input
                  type="color"
                  value={formData.color || '#6B7280'}
                  onChange={(e) => setFormData(prev => ({ ...prev, color: e.target.value }))}
                  className="w-16 h-16 rounded-lg border-2 border-spartan-300 cursor-pointer"
                />
                <Input
                  name="color"
                  value={formData.color || '#6B7280'}
                  onChange={handleChange}
                  placeholder="#6B7280"
                  className="flex-1"
                />
              </div>
              <div className="mt-2 flex flex-wrap gap-2">
                {CATEGORY_COLORS.map((color) => (
                  <button
                    key={color}
                    type="button"
                    onClick={() => handleColorSelect(color)}
                    className="w-8 h-8 rounded-full border-2 border-spartan-300 hover:border-spartan-500 transition-colors"
                    style={{ backgroundColor: color }}
                    title={color}
                  />
                ))}
              </div>
            </div>

            <Button
              type="submit"
              variant="primary"
              fullWidth
              isLoading={isSubmitting}
              disabled={isSubmitting}
            >
              {t('categories.createCategory')}
            </Button>
          </form>
        </Card>
      )}

      {/* Mostrar todas las categorías juntas */}
      <Card title="Mis Categorías">
        {userCategories.length === 0 ? (
          <p className="text-sm text-spartan-600 text-center py-8">
            No has creado ninguna categoría personalizada. Crea una para comenzar.
          </p>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {userCategories.map((category) => (
              <div
                key={category.id}
                className="flex items-center justify-between p-4 rounded-lg border-2 transition-all hover:shadow-md"
                style={{
                  borderColor: category.color || '#E5E7EB',
                  backgroundColor: category.color ? `${category.color}10` : '#F9FAFB',
                }}
              >
                <div className="flex items-center gap-3 flex-1 min-w-0">
                  <div
                    className="w-12 h-12 rounded-lg flex items-center justify-center text-xl flex-shrink-0"
                    style={{ backgroundColor: category.color || '#6B7280' }}
                  >
                    {category.icon || '📁'}
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="font-medium text-spartan-900 truncate">{category.name}</p>
                    {category.code && (
                      <p className="text-xs text-spartan-600">({category.code})</p>
                    )}
                    <p className="text-xs text-spartan-500 mt-1">
                      {category.type === 'INCOME' ? 'Para Ingresos' : 'Para Egresos'}
                    </p>
                  </div>
                </div>
                <Button
                  variant="danger"
                  size="sm"
                  onClick={() => handleDelete(category.id, category.isSystem)}
                  className="ml-2 flex-shrink-0"
                >
                  {t('common.delete')}
                </Button>
              </div>
            ))}
          </div>
        )}
      </Card>
    </div>
  );
};
